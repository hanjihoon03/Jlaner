package com.jlaner.project.service;

import com.jlaner.project.config.jwt.TokenProvider;
import com.jlaner.project.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class TokenService {
    private final TokenProvider tokenProvider;
    private final RefreshTokenRedisService refreshTokenRedisService;
    private final MemberService memberService;


    public String createNewAccessToken(String refreshToken) {
        if(!tokenProvider.validToken(refreshToken)) {
            throw new IllegalArgumentException("Unexpected token");
        }

        Long memberId = refreshTokenRedisService.findByRefreshToken(refreshToken).getMemberId();
        Member member = memberService.findByMemberId(memberId);

        return tokenProvider.generateToken(member, Duration.ofHours(2));
    }
}
