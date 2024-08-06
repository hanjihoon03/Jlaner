package com.jlaner.project.controller;


import com.jlaner.project.config.jwt.TokenProvider;
import com.jlaner.project.config.outh2.OAuth2SuccessHandler;
import com.jlaner.project.domain.RefreshToken;
import com.jlaner.project.service.RefreshTokenRedisService;
import com.jlaner.project.util.CookieUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;


@RequiredArgsConstructor
@RestController
@Slf4j
public class TokenApiController {

    private final RefreshTokenRedisService refreshTokenRedisService;
    private final TokenProvider tokenProvider;

    @GetMapping("/api/auth")
    @PreAuthorize("isAuthenticated()")
    public String getAuthenticated() {
        return "Authenticated";
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        // 쿠키에서 리프레시 토큰 추출
        String refreshToken = CookieUtil.getCookie(request, OAuth2SuccessHandler.REFRESH_TOKEN_COOKIE_NAME)
                .map(Cookie::getValue)
                .orElse(null);

        if (refreshToken != null) {
            // Redis에서 리프레시 토큰 삭제
            RefreshToken token = refreshTokenRedisService.findByRefreshToken(refreshToken);
            if (token != null) {
                refreshTokenRedisService.deleteByMemberId(token.getMemberId());
            }
        }

        // 쿠키 삭제
        CookieUtil.deleteCookie(request, response, OAuth2SuccessHandler.REFRESH_TOKEN_COOKIE_NAME);
    }

    @GetMapping("/api/check-auth")
    public ResponseEntity<?> tokenCheck(@RequestHeader("Authorization") String token) {
        try {
            if (token != null && token.startsWith("Bearer ")) {
                String jwtToken = token.substring(7);
                Claims claims = tokenProvider.getClaims(jwtToken);
                String memberName = claims.getSubject();
                log.info("memberName={}", memberName);

                // 인증된 사용자 정보 반환
                return ResponseEntity.ok().body(Collections.singletonMap("memberName", memberName));
            } else {
                return ResponseEntity.status(401).body("Unauthorized");
            }
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid token");
        }
    }

}
