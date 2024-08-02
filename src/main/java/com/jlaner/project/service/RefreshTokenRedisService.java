package com.jlaner.project.service;

import com.jlaner.project.domain.RefreshToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class RefreshTokenRedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    //리프레시 토큰을 별도의 인덱스로 검색할 수 있게끔 하기 위한 스태틱 필드
    private static final String REFRESH_TOKEN_INDEX_PREFIX = "refreshTokenIndex:";
    private HashOperations<String, String, Object> hashOps() {
        return redisTemplate.opsForHash();
    }

    public void saveToken(RefreshToken refreshToken) {
        String key = String.valueOf(refreshToken.getMemberId());
        hashOps().put(key, "id", refreshToken.getId());
        //레디스에서는 Long을 저장할때 String으로 저장하지 않으면 Integer로 저장될 수 있기 때문에 ClassCastException이 발생할 수 있다.
        //그러므로 memberId를 String으로 저장하고 꺼내 사용할 때 Long으로 변환.
        hashOps().put(key, "memberId", String.valueOf(refreshToken.getMemberId()));
        hashOps().put(key, "accessToken", refreshToken.getAccessToken());
        hashOps().put(key, "refreshToken", refreshToken.getRefreshToken());

        // refreshToken을 키로 하는 인덱스 저장
        redisTemplate.opsForValue().set(REFRESH_TOKEN_INDEX_PREFIX + refreshToken.getRefreshToken(), key);
    }


    public RefreshToken findByMemberId(Long memberId) {
        // Redis의 키를 memberId로 설정하여 검색
        String key = String.valueOf(memberId);
        return toRefreshToken(key);
    }
    public RefreshToken findByRefreshToken(String refreshToken) {
        String memberIdKey = (String) redisTemplate.opsForValue().get(REFRESH_TOKEN_INDEX_PREFIX + refreshToken);
        if (memberIdKey == null) {
            return null;
        }
        return toRefreshToken(memberIdKey);
    }
    public void deleteByMemberId(Long memberId) {
        // Redis의 키를 memberId로 설정하여 삭제
        redisTemplate.delete(String.valueOf(memberId));
    }
    public void deleteByAccessToken(String accessToken) {
        // Redis의 키를 memberId로 설정하여 삭제
        redisTemplate.delete(accessToken);
    }

    private RefreshToken toRefreshToken(String key) {
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);

        String id = (String) entries.get("id");
        Object memberIdObj = entries.get("memberId");
        Long memberId = null;
        if (memberIdObj != null) {
            if (memberIdObj instanceof Integer) {
                memberId = ((Integer) memberIdObj).longValue();
            } else if (memberIdObj instanceof Long) {
                memberId = (Long) memberIdObj;
            } else if (memberIdObj instanceof String) {
                memberId = Long.valueOf((String) memberIdObj); // String을 Long으로 변환
            }
        }
        String accessToken = (String) entries.get("accessToken");
        String refreshToken = (String) entries.get("refreshToken");

        if (id == null || memberId == null || accessToken == null || refreshToken == null) {
            return null;
        }

        return new RefreshToken(id, memberId, accessToken, refreshToken);
    }

}
