package com.jlaner.project.Token;


import com.jlaner.project.config.jwt.JwtProperties;
import com.jlaner.project.config.jwt.TokenProvider;
import com.jlaner.project.domain.Member;
import com.jlaner.project.domain.MemberRole;
import com.jlaner.project.repository.MemberRepository;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;


import java.time.Duration;
import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Slf4j
public class TokenProviderTest {

    @Autowired
    TokenProvider tokenProvider;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    JwtProperties jwtProperties;

    @DisplayName("유저 정보와 만료 기간을 전달해 토큰을 만들 수 있다.")
    @Test
    void generateToken() {
        //given
        Member member = new Member("test", "1234", "tester", MemberRole.USER);
        memberRepository.save(member);

        //when
        String token = tokenProvider.generateToken(member, Duration.ofDays(14));
        log.info("token = {}", token);

        //then
        Long userId = Jwts.parserBuilder()
                .setSigningKey(jwtProperties.getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("id", Long.class);

        log.info("userId = {}", userId);

        assertThat(userId).isEqualTo(member.getId());

    }

    @DisplayName("만료된 토큰인 때에 유효성 검증에 실패한다.")
    @Test
    void validToken_invalidToken() {
        //given
        String token = JwtFactory.builder()
                .expiration(new Date(new Date().getTime() - Duration.ofDays(7).toMillis()))
                .build()
                .createToken(jwtProperties);

        //when
        boolean result = tokenProvider.validToken(token);
        log.info("token={}",token);
        //then
        assertThat(result).isFalse();
    }

    @DisplayName("토큰 기반으로 인증 정보를 가져올 수 있다.")
    @Test
    void getAuthentication() {
        //given
        String loginId = "test";
        String token = JwtFactory.builder()
                .subject(loginId)
                .build()
                .createToken(jwtProperties);

        //when
        Authentication authentication = tokenProvider.getAuthentication(token);

        //then
        assertThat(((UserDetails) authentication.getPrincipal()).getUsername()).isEqualTo(loginId);
    }

    @DisplayName("토큰으로 유저 ID를 가져올 수 있다.")
    @Test
    void getUserId() {
        //given
        Long userId = 1L;
        String token = JwtFactory.builder()
                .claims(Map.of("id", userId))
                .build()
                .createToken(jwtProperties);

        //when
        Long userIdByToken = tokenProvider.getMemberId(token);

        //then
        assertThat(userIdByToken).isEqualTo(userId);
    }
}
