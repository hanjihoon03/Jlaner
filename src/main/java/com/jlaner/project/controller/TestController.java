package com.jlaner.project.controller;

import com.jlaner.project.config.jwt.TokenProvider;
import com.jlaner.project.domain.Member;
import com.jlaner.project.domain.Post;
import com.jlaner.project.domain.RefreshToken;
import com.jlaner.project.domain.ScheduleData;
import com.jlaner.project.dto.PostDto;
import com.jlaner.project.service.MemberService;
import com.jlaner.project.service.RefreshTokenRedisService;
import com.jlaner.project.util.CookieUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.security.Principal;
import java.time.Duration;

@Controller
@Slf4j
@RequiredArgsConstructor
public class TestController {
    private final TokenProvider tokenProvider;
    private final MemberService memberService;
    private final RefreshTokenRedisService refreshTokenRedisService;

    @GetMapping("")
    public String movelogin(){
        return "login/login";
    }

    @GetMapping("/login")
    public String login() {

        return "login/login";
    }
    @GetMapping("/home")
    public String home(@RequestParam("token") String token,
                       HttpServletRequest request,
                       HttpServletResponse response,
                       Model model) {
        // accessToken을 로그에 출력하여 확인
        log.info("accessToken={}", token);

        try {
            Long memberId;
            String newAccessToken = null;

            if (tokenProvider.isTokenExpired(token)) {
                log.info("토큰이 만료되었습니다. 리프레시 토큰을 사용하여 새로운 액세스 토큰을 발급합니다.");
                String refreshTokenValue = CookieUtil.getCookie(request, "refresh_token")
                        .map(Cookie::getValue)
                        .orElse(null);

                if (refreshTokenValue != null) {
                    RefreshToken refreshToken = refreshTokenRedisService.findByRefreshToken(refreshTokenValue);
                    if (refreshToken != null) {
                        memberId = refreshToken.getMemberId();
                        Member findByMember = memberService.findByMemberId(memberId);

                        newAccessToken = tokenProvider.generateToken(findByMember, Duration.ofDays(1));
                        //새로운 액세스 토큰을 발급해야 하기 때문에 redis에 저장된 토큰 정보의 AccessToken를 업데이트
                        RefreshToken updateToken = refreshToken.accessTokenUpdate(newAccessToken);
                        refreshTokenRedisService.saveToken(updateToken);

                        response.setHeader("Authorization", "Bearer " + newAccessToken);
                        log.info("새로운 액세스 토큰 발급={}", newAccessToken);

                        // 새로운 토큰을 포함하여 리다이렉트
                        String targetUrl = UriComponentsBuilder.fromUriString("/home")
                                .queryParam("token", newAccessToken)
                                .build()
                                .toUriString();
                        return "redirect:" + targetUrl;
                    } else {
                        log.error("유효하지 않은 리프레시 토큰입니다.");
                        return "redirect:/login?error=invalidRefreshToken";
                    }
                } else {
                    log.error("리프레시 토큰이 존재하지 않습니다.");
                    return "redirect:/login?error=missingRefreshToken";
                }
            } else {
                memberId = tokenProvider.getMemberId(token);
            }

            Member findMember = memberService.findByMemberId(memberId);

            model.addAttribute("memberName", findMember.getName());
            model.addAttribute("scheduleData", new ScheduleData());
            model.addAttribute("contentData", "");
        } catch (Exception e) {
            log.error("오류 발생: {}", e.getMessage());
            return "redirect:/login?error=unauthorized";
        }
        log.info("home 이동");

        return "home";
    }

    @GetMapping("/testPage")
    @PreAuthorize("isAuthenticated()")
    public String testPage() {
        log.info("testPage");

        return "testPage";
    }

}
