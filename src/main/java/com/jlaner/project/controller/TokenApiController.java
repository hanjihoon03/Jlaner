package com.jlaner.project.controller;


import com.jlaner.project.config.outh2.OAuth2SuccessHandler;
import com.jlaner.project.domain.RefreshToken;
import com.jlaner.project.dto.CreateAccessTokenRequest;
import com.jlaner.project.dto.CreateAccessTokenResponse;
import com.jlaner.project.dto.StatusResponseDto;
import com.jlaner.project.service.RefreshTokenRedisService;
import com.jlaner.project.service.TokenService;
import com.jlaner.project.util.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@Slf4j
public class TokenApiController {

    private final RefreshTokenRedisService refreshTokenRedisService;

//    @GetMapping("/testPage")
//    public ResponseEntity<Map<String, Object>> getTestPage() {
//        Map<String, Object> response = new HashMap<>();
//        response.put("message", "Success");
//        return ResponseEntity.ok(response);
//    }

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

}
