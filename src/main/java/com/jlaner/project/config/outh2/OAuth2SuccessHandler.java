package com.jlaner.project.config.outh2;

import com.jlaner.project.config.jwt.TokenProvider;
import com.jlaner.project.domain.Member;
import com.jlaner.project.domain.RefreshToken;
import com.jlaner.project.repository.RefreshTokenRepository;
import com.jlaner.project.service.MemberService;
import com.jlaner.project.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;

/**
 * OAuth2 로그인 성공 시 처리하는 핸들러 클래스입니다.
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    // 리프레시 토큰을 저장할 쿠키의 이름
    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
    // 리프레시 토큰의 유효 기간 (14일)
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);
    // 액세스 토큰의 유효 기간 (1일)
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofDays(1);
    // 인증 성공 후 리다이렉트할 기본 경로
    public static final String REDIRECT_PATH = "/home";

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;
    private final MemberService memberService;

    /**
     * 인증 성공 시 호출되는 메서드입니다.
     *
     * @param request  HttpServletRequest 객체
     * @param response HttpServletResponse 객체
     * @param authentication 인증 정보
     * @throws IOException 입출력 예외 발생 시
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = extractEmail(oAuth2User.getAttributes());
        Member member = memberService.findByEmail(email);
        log.info("member={}", member);
        log.info("-------2--------");

        // 리프레시 토큰 생성 및 저장
        String refreshToken = tokenProvider.generateToken(member, REFRESH_TOKEN_DURATION);
        saveRefreshToken(member.getId(), refreshToken);
        addRefreshTokenToCookie(request, response, refreshToken);

        // 액세스 토큰 생성
        String accessToken = tokenProvider.generateToken(member, ACCESS_TOKEN_DURATION);


        String targetUrl = getTargetUrl(accessToken);

        clearAuthenticationAttributes(request, response);


        try {
            // 인증 성공 후 리다이렉트
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
            log.info("Response status: {}", response.getStatus());
            log.info("Response headers: {}", response.getHeaderNames());
            log.info("Response locale: {}", response.getLocale());

        } catch (Exception e) {
            log.error("Error during redirect", e);
            throw e;
        }
    }

    /**
     * 리프레시 토큰을 저장합니다.
     *
     * @param memberId 사용자 ID
     * @param newRefreshToken 새로운 리프레시 토큰
     */
    private void saveRefreshToken(Long memberId, String newRefreshToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByMemberId(memberId)
                .map(entity -> entity.update(newRefreshToken))
                .orElse(new RefreshToken(memberId, newRefreshToken));

        refreshTokenRepository.save(refreshToken);
    }

    /**
     * 리프레시 토큰을 쿠키에 추가합니다.
     *
     * @param request HttpServletRequest 객체
     * @param response HttpServletResponse 객체
     * @param refreshToken 리프레시 토큰
     */
    private void addRefreshTokenToCookie(HttpServletRequest request, HttpServletResponse response, String refreshToken) {
        int cookieMaxAge = (int) REFRESH_TOKEN_DURATION.toSeconds();

        // 기존 쿠키 삭제 후 새로운 쿠키 추가
        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN_COOKIE_NAME);
        CookieUtil.addCookie(response, REFRESH_TOKEN_COOKIE_NAME, refreshToken, cookieMaxAge);
        log.info("Refresh token added to cookie with max age: {}", cookieMaxAge);
    }

    /**
     * 인증 관련 속성을 정리합니다.
     *
     * @param request HttpServletRequest 객체
     * @param response HttpServletResponse 객체
     */
    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }
    private String getTargetUrl(String token) {
        return UriComponentsBuilder.fromUriString(REDIRECT_PATH)
                .queryParam("token", token)
                .build()
                .toUriString();
    }

    private String extractEmail(Map<String, Object> attributes) {
        //카카오 이메일  추출
        if (attributes.containsKey("kakao_account")) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            if (kakaoAccount.containsKey("email")) {
                return (String) kakaoAccount.get("email");
            }
        }

        // 구글과 같은 다른 서비스의 이메일 추출
        if (attributes.containsKey("email")) {
            return (String) attributes.get("email");
        }

        // 네이버의 이메일 추출
        if (attributes.containsKey("response")) {
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");
            if (response.containsKey("email")) {
                return (String) response.get("email");
            }
        }
        throw new IllegalArgumentException("Cannot extract email from OAuth2 attributes");
    }
}