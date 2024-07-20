//package com.jlaner.project.config;
//
//import com.jlaner.project.config.jwt.TokenProvider;
//import com.jlaner.project.config.outh2.OAuth2AuthorizationRequestBasedOnCookieRepository;
//import com.jlaner.project.config.outh2.OAuth2SuccessHandler;
//import com.jlaner.project.config.outh2.OAuth2UserCustomService;
//import com.jlaner.project.repository.RefreshTokenRepository;
//import com.jlaner.project.service.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.HttpStatusEntryPoint;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//
//@RequiredArgsConstructor
//@Configuration
//public class WebOAuthSecurityConfig {
//
//
//    private final OAuth2UserCustomService oAuth2UserCustomService;
//    private final TokenProvider tokenProvider;
//    private final RefreshTokenRepository refreshTokenRepository;
//    private final UserService userService;
//
//
//    /**
//     * 스프링 시큐리티에서 특정 요청을 무시하도록 설정합니다.
//     *
//     * @return WebSecurityCustomizer 객체
//     */
//    @Bean
//    public WebSecurityCustomizer configure() {
//        return (web) -> web.ignoring()
//                .requestMatchers(
//                        new AntPathRequestMatcher("/img/**"),
//                        new AntPathRequestMatcher("/css/**"),
//                        new AntPathRequestMatcher("/js/**")
//                );
//    }
//
//    /**
//     * HTTP 보안 설정을 구성합니다.
//     *
//     * @param http HttpSecurity 객체
//     * @return SecurityFilterChain 객체
//     * @throws Exception 예외 발생 시
//     */
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        return http
//                .csrf(AbstractHttpConfigurer::disable) // CSRF 보호 비활성화
//                .httpBasic(AbstractHttpConfigurer::disable) // HTTP Basic 인증 비활성화
//                .formLogin(AbstractHttpConfigurer::disable) // 폼 로그인 비활성화
//                .logout(AbstractHttpConfigurer::disable) // 로그아웃 비활성화
//                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 상태를 무상태로 설정
//                .addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class) // 토큰 인증 필터 추가
//                .authorizeRequests(auth -> auth
//                        .requestMatchers(new AntPathRequestMatcher("/login")).permitAll() // /login 엔드포인트는 모든 사용자에게 허용
//                        .requestMatchers(new AntPathRequestMatcher("/")).authenticated() // / 엔드포인트는 인증된 사용자만 허용
//                        .anyRequest().permitAll()) // 그 외 모든 요청은 허용
//                .oauth2Login(oauth2 -> oauth2
//                        .loginPage("/login") // 커스텀 로그인 페이지 경로
//                        .authorizationEndpoint(authorizationEndpoint -> authorizationEndpoint.authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository())) // 인증 요청을 쿠키에 기반하여 저장
//                        .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint.userService(oAuth2UserCustomService)) // 사용자 정보를 로드할 서비스 설정
//                        .successHandler(oAuth2SuccessHandler()) // 인증 성공 핸들러 설정
//                )
//                .exceptionHandling(exceptionHandling -> exceptionHandling
//                        .defaultAuthenticationEntryPointFor(
//                                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
//                                new AntPathRequestMatcher("/api/**")
//                        ))
//                .build();
//    }
//
//    /**
//     * OAuth2 인증 성공 핸들러를 빈으로 등록합니다.
//     *
//     * @return OAuth2SuccessHandler 객체
//     */
//    @Bean
//    public OAuth2SuccessHandler oAuth2SuccessHandler() {
//        return new OAuth2SuccessHandler(tokenProvider,
//                refreshTokenRepository,
//                oAuth2AuthorizationRequestBasedOnCookieRepository(),
//                userService
//        );
//    }
//
//    /**
//     * 토큰 인증 필터를 빈으로 등록합니다.
//     *
//     * @return TokenAuthenticationFilter 객체
//     */
//    @Bean
//    public TokenAuthenticationFilter tokenAuthenticationFilter() {
//        return new TokenAuthenticationFilter(tokenProvider);
//    }
//
//    /**
//     * OAuth2AuthorizationRequestBasedOnCookieRepository를 빈으로 등록합니다.
//     *
//     * @return OAuth2AuthorizationRequestBasedOnCookieRepository 객체
//     */
//    @Bean
//    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
//        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
//    }
//
//    /**
//     * BCryptPasswordEncoder를 빈으로 등록합니다.
//     *
//     * @return BCryptPasswordEncoder 객체
//     */
//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}