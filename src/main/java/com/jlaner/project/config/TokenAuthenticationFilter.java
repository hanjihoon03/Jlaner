package com.jlaner.project.config;

import com.jlaner.project.config.jwt.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;
    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String TOKEN_PREFIX = "Bearer ";
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

        // HTTP 요청 헤더에서 Authorization 헤더 값을 가져옴
        String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);
        log.info("authorizationHeader ={}", authorizationHeader);
        // Authorization 헤더 값에서 토큰을 추출
        String token = getAccessToken(authorizationHeader);
        log.info("token = {}", token);
        log.info("-------4--------");
        // 추출된 토큰이 유효한 경우
        if (tokenProvider.validToken(token)) {
            log.info("인증 성공");
            // 토큰을 사용하여 인증 객체를 가져옴
            Authentication authentication = tokenProvider.getAuthentication(token);
            // SecurityContext에 인증 객체를 설정하여 인증 상태를 유지
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 다음 필터로 요청과 응답을 전달
        filterChain.doFilter(request, response);
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
