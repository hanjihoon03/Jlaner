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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;
    private final RefreshTokenRedisService refreshTokenRedisService;
    private final MemberRepository memberRepository;
    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofDays(1);
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

        String requestURI = request.getRequestURI();

        // 로그인 페이지와 정적 리소스에 대해서는 필터를 무시
        if (requestURI.startsWith("/css") || requestURI.startsWith("/pngs")) {
            filterChain.doFilter(request, response);
            return;
        }

        // HTTP 요청 헤더에서 Authorization 헤더 값을 가져옴
        String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);
        log.info("authorizationHeader ={}", authorizationHeader);
        // Authorization 헤더 값에서 토큰을 추출
        String token = getAccessToken(authorizationHeader);
        log.info("token = {}", token);
        log.info("-------4--------");

        try {
        //AccessToken이 만료되었는지 확인
        if (token != null && !tokenProvider.validToken(token)) {
            log.info("Access Token 만료");
            //만료라면 쿠키에서 리프레시 토큰 값을 가져온다.
            String refreshTokenValue = CookieUtil.getCookie(request, REFRESH_TOKEN_COOKIE_NAME)
                    .map(Cookie::getValue)
                    .orElse(null);
            log.info("refreshToken={}", refreshTokenValue);

            //리프레시 토큰 값이 null이 아니라면 쿠키의 refreshToken과 레디스의 refreshToken이 같은지 추가 검증
            if (refreshTokenValue != null) {
                //쿠키의 리프레시토큰 값이 레디스에 있다면 검증된 값이기에 진행
                RefreshToken refreshToken = refreshTokenRedisService.findByRefreshToken(refreshTokenValue);

                //반환되는 리프레시 토큰 값이 존재하고 valid로 만료를 검증해서 검증시 액세스 토큰을 발급
                if (refreshToken != null && tokenProvider.validToken(refreshTokenValue)) {
                    Member member = memberRepository.findById(refreshToken.getMemberId())
                            .orElse(null);

                    if (member != null) {
                        String newAccessToken = tokenProvider.generateToken(member, ACCESS_TOKEN_DURATION);
                        //새로 발급한 액세스 토큰을 업데이트 하고 레디스에 저장.
                        refreshToken.accessTokenUpdate(newAccessToken);
                        refreshTokenRedisService.saveToken(refreshToken);

                        log.info("새로운 액세스 토큰 발급={}", newAccessToken);

                        response.setHeader(HEADER_AUTHORIZATION, TOKEN_PREFIX + newAccessToken);
                        // 새로운 액세스 토큰으로 인증 객체 생성 및 설정
                        Authentication authentication = tokenProvider.getAuthentication(newAccessToken);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    } else {
                        log.info("Not Found Member");
                        throw new CustomUnauthorizedException("Unauthorized");
                    }
                    } else {
                        //토큰이 null이거나 만료라면 다시 로그인
                        //401 오류 페이지를 로그인 페이지로 이동하게끔 구성
                        log.info("Invalid Refresh Token");
                        throw new CustomUnauthorizedException("Unauthorized");
                    }
                } else {
                    //쿠키의 리프레시 토큰이 유효하지 않은 경우 (만료 또는 잘못된 토큰)
                    log.info("Invalid Refresh Token");
                    throw new CustomUnauthorizedException("Unauthorized");
                }
            }
//        // 현재 SecurityContext에 인증 정보가 있는지 확인
//        //인증되지 않은 사용자라면 custom Exception 반환
//        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
//        if (currentAuth == null || !currentAuth.isAuthenticated()) {
//            log.info("User is not authenticated");
//            throw new CustomUnauthorizedException("Unauthorized");
//        }


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
        }catch (CustomUnauthorizedException e) {
            response.sendRedirect("/login");
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
