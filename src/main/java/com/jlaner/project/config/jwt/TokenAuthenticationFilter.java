package com.jlaner.project.config.jwt;


import com.jlaner.project.domain.Member;
import com.jlaner.project.domain.RefreshToken;
import com.jlaner.project.exception.CustomUnauthorizedException;
import com.jlaner.project.repository.MemberRepository;
import com.jlaner.project.service.RefreshTokenRedisService;
import com.jlaner.project.util.CookieUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;


@RequiredArgsConstructor
@Slf4j
@Order(2)
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;
    private final RefreshTokenRedisService refreshTokenRedisService;
    private final MemberRepository memberRepository;
    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofDays(1);
    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String TOKEN_PREFIX = "Bearer ";
    private int count =0;
    /**
     * 요청을 필터링하여 JWT 토큰을 검증하고 인증 정보를 설정하는 메서드
     *
     * @param request     클라이언트에서 온 HTTP 요청
     * @param response    서버로부터 클라이언트로 응답
     * @param filterChain 다음 필터로 진행할 수 있도록 체인을 제공하는 객체
     * @throws ServletException 필터 처리 중 발생할 수 있는 예외
     * @throws IOException      입출력 예외
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        //정적 리소스에 대해서는 필터를 무시
        if (requestURI.startsWith("/css") || requestURI.startsWith("/pngs") || requestURI.startsWith("/js")) {
            filterChain.doFilter(request, response);
            return;
        }
        log.info("Incoming request: URI = {}, Method = {}", request.getRequestURI(), request.getMethod());
        log.info("request={}", request);
        log.info("requestURI={}", request.getRequestURI());


        // HTTP 요청 헤더에서 Authorization 헤더 값을 가져옴
        String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);
        log.info("authorizationHeader ={}", authorizationHeader);
        // Authorization 헤더 값에서 토큰을 추출
        String token = getAccessToken(authorizationHeader);
        log.info("token = {}", token);

        // 추출된 토큰이 유효한 경우
        if (tokenProvider.validToken(token)) {
            log.info("인증 성공");
            // 토큰을 사용하여 인증 객체를 가져옴
            Authentication authentication = tokenProvider.getAuthentication(token);
            // SecurityContext에 인증 객체를 설정하여 인증 상태를 유지
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
            return;
        }


        if (token != null && !tokenProvider.validToken(token)) {
            log.info("Access Token 만료");
            handleExpiredAccessToken(request, response, filterChain);
            return;
        }

        // 다음 필터로 요청과 응답을 전달
        filterChain.doFilter(request, response);
}

    private void handleExpiredAccessToken(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String refreshTokenValue = CookieUtil.getCookie(request, REFRESH_TOKEN_COOKIE_NAME)
                .map(Cookie::getValue)
                .orElse(null);
        log.info("Refresh Token = {}", refreshTokenValue);

        if (refreshTokenValue != null) {
            RefreshToken refreshToken = refreshTokenRedisService.findByRefreshToken(refreshTokenValue);

            if (refreshToken != null && tokenProvider.validToken(refreshTokenValue)) {
                Member member = memberRepository.findById(refreshToken.getMemberId()).orElse(null);

                if (member != null) {
                    String newAccessToken = tokenProvider.generateToken(member, ACCESS_TOKEN_DURATION);
                    refreshToken.accessTokenUpdate(newAccessToken);
                    refreshTokenRedisService.saveToken(refreshToken);

                    log.info("새로운 액세스 토큰 발급 = {}", newAccessToken);

                    response.setHeader(HEADER_AUTHORIZATION, TOKEN_PREFIX + newAccessToken);

                    Authentication authentication = tokenProvider.getAuthentication(newAccessToken);
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    // 발급 후 필터 체인 진행
                    filterChain.doFilter(request, response);
                } else {
                    log.info("Not Found Member");
                    response.sendRedirect("/login?error=memberNotFound");
                }
            } else {
                log.info("Invalid Refresh Token");
                response.sendRedirect("/login?error=invalidRefreshToken");
            }
        } else {
            log.info("Missing Refresh Token");
            response.sendRedirect("/login?error=missingRefreshToken");
        }
    }



    /**
     * Authorization 헤더에서 JWT 토큰을 추출하는 메서드
     *
     * @param authorizationHeader Authorization 헤더 값
     * @return 추출된 JWT 토큰
     */
    private String getAccessToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
            return authorizationHeader.substring(TOKEN_PREFIX.length());
        }
        return null;
    }
}
