package com.jlaner.project.config.jwt;


import com.jlaner.project.domain.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class TokenProvider {
    private final JwtProperties jwtProperties;

    /**
     * JWT 토큰 생성 메서드
     *
     * @param member      JWT에 포함할 사용자 정보
     * @param expiredAt 토큰의 유효 기간
     * @return 생성된 JWT 토큰
     */
    public String generateToken(Member member, Duration expiredAt) {
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), member);
    }

    /**
     * JWT 토큰을 실제로 생성하는 메서드
     *
     * @param expiry 만료 시간
     * @param member   JWT에 포함할 사용자 정보
     * @return 생성된 JWT 토큰
     */
    private String makeToken(Date expiry, Member member) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer("test")
                .setIssuedAt(now)
                .setExpiration(expiry)
                .setSubject(member.getName())
                .claim("id", member.getId())
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }

    /**
     * 주어진 JWT 토큰의 유효성을 검사하는 메서드
     *
     * @param token 검사할 JWT 토큰
     * @return 토큰의 유효성 여부 (유효하면 true, 그렇지 않으면 false)
     */
    public Boolean validToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(jwtProperties.getSecretKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * JWT 토큰에서 인증 객체를 가져오는 메서드
     *
     * @param token JWT 토큰
     * @return 인증 객체 (Spring Security의 UsernamePasswordAuthenticationToken)
     */
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("USER"));

        return new UsernamePasswordAuthenticationToken(
                new org.springframework.security.core.userdetails.User(claims.getSubject(), "", authorities),
                token,
                authorities);
    }

    /**
     * JWT 토큰에서 사용자 ID를 가져오는 메서드
     *
     * @param token JWT 토큰
     * @return 사용자 ID
     */
    public Long getMemberId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }

    /**
     * 주어진 JWT 토큰에서 클레임(Claims)을 추출하는 메서드
     *
     * @param token JWT 토큰
     * @return 추출된 클레임 객체
     */
    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtProperties.getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}