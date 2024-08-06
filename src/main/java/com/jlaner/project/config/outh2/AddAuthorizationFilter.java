//package com.jlaner.project.config.outh2;
//
//import com.jlaner.project.domain.RefreshToken;
//import com.jlaner.project.service.RefreshTokenRedisService;
//import com.jlaner.project.util.CookieUtil;
//import jakarta.servlet.*;
//import jakarta.servlet.annotation.WebFilter;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.core.annotation.Order;
//
//import java.io.IOException;
//
//
//@WebFilter("/*")
//@Slf4j
//@RequiredArgsConstructor
//@Order(1)
//public class AddAuthorizationFilter implements Filter {
//    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
//    private final RefreshTokenRedisService refreshTokenRedisService;
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
//        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
//        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
//
//
//        String requestURI = httpServletRequest.getRequestURI();
//
//        if (requestURI.startsWith("/css") || requestURI.startsWith("/pngs") || requestURI.startsWith("/js")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        String refreshTokenValue = CookieUtil.getCookie(httpServletRequest, REFRESH_TOKEN_COOKIE_NAME)
//                .map(Cookie::getValue)
//                .orElse(null);
//        log.info("refreshToken={}", refreshTokenValue);
//        if (refreshTokenValue != null) {
//            try {
//                RefreshToken refreshToken = refreshTokenRedisService.findByRefreshToken(refreshTokenValue);
//                if (refreshToken != null && refreshToken.getAccessToken() != null) {
//                    httpServletResponse.setHeader("Authorization", "Bearer " + refreshToken.getAccessToken());
//                    log.info("헤더를 설정합니다. {}", refreshToken.getAccessToken());
//                } else {
//                    log.info("유효한 access token을 찾을 수 없습니다.");
//                }
//            } catch (Exception e) {
//                log.error("refreshToken으로부터 accessToken을 가져오는 중 오류 발생: ", e);
//            }
//        } else {
//            log.info("Authorization을 설정하기 위해 필요한 refreshToken이 null 입니다.");
//        }
//        filterChain.doFilter(request, response);
//    }
//
//}
