package com.jlaner.project.config.outh2;

import com.jlaner.project.util.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.web.util.WebUtils;

/**
 * 이 클래스는 쿠키를 사용하여 OAuth2 인증 요청을 저장하고 로드하는 역할을 합니다.
 * AuthorizationRequestRepository 인터페이스를 구현하여 OAuth2AuthorizationRequest 객체를 관리합니다.
 */
@Slf4j
public class OAuth2AuthorizationRequestBasedOnCookieRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    // OAuth2 인증 요청을 저장할 쿠키의 이름
    public final static String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
    // 쿠키의 만료 시간 (초 단위)
    private final static int COOKIE_EXPIRE_SECONDS = 18000;

    /**
     * 쿠키에서 OAuth2 인증 요청을 로드하고 이를 제거합니다.
     *
     * @param request  HttpServletRequest 객체
     * @param response HttpServletResponse 객체
     * @return OAuth2AuthorizationRequest 객체 (존재하는 경우), 그렇지 않으면 null
     */
    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        return this.loadAuthorizationRequest(request);
    }

    /**
     * 쿠키에서 OAuth2 인증 요청을 로드합니다.
     *
     * @param request HttpServletRequest 객체
     * @return OAuth2AuthorizationRequest 객체 (존재하는 경우), 그렇지 않으면 null
     */
    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        return CookieUtil.deserialize(cookie, OAuth2AuthorizationRequest.class);
    }

    /**
     * OAuth2 인증 요청을 쿠키에 저장합니다.
     *
     * @param authorizationRequest 저장할 OAuth2AuthorizationRequest 객체
     * @param request              HttpServletRequest 객체
     * @param response             HttpServletResponse 객체
     */
    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {

        // authorizationRequest가 null이면 쿠키를 제거합니다.
        if (authorizationRequest == null) {
            removeAuthorizationRequestCookies(request, response);
            return;
        }
        log.info("-------3--------");
        // OAuth2 인증 요청을 쿠키에 추가합니다.
        CookieUtil.addCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME, CookieUtil.serialize(authorizationRequest), COOKIE_EXPIRE_SECONDS);
    }

    /**
     * OAuth2 인증 요청 쿠키를 제거합니다.
     *
     * @param request  HttpServletRequest 객체
     * @param response HttpServletResponse 객체
     */
    public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
    }
}
