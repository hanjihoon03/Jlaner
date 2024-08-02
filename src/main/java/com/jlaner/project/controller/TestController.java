package com.jlaner.project.controller;

import com.jlaner.project.config.jwt.TokenProvider;
import com.jlaner.project.domain.Member;
import com.jlaner.project.domain.Post;
import com.jlaner.project.domain.ScheduleData;
import com.jlaner.project.service.MemberService;
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

import java.security.Principal;

@Controller
@Slf4j
@RequiredArgsConstructor
public class TestController {
    private final TokenProvider tokenProvider;
    private final MemberService memberService;

    @GetMapping("")
    public String moveHome() {
        return "home";
    }

    @GetMapping("/login")
    public String login() {

        return "login/login";
    }
    @GetMapping("/home")
    public String home(@RequestParam("token") String token,
                       HttpServletResponse response,
                       Model model) {
        // accessToken을 로그에 출력하여 확인
        log.info("accessToken={}", token);

        // accessToken을 이용하여 사용자 정보를 조회
        Long memberId = tokenProvider.getMemberId(token);
        Member findMember = memberService.findByMemberId(memberId);
        response.setHeader("Authorization", "Bearer " + token);

        // 사용자 이름을 모델에 추가
        model.addAttribute("memberName", findMember.getName());
        model.addAttribute("scheduleData", new ScheduleData());
        model.addAttribute("post", new Post());

        // home 뷰 반환
        return "home";
    }
    @GetMapping("/testPage")
    @PreAuthorize("isAuthenticated()")
    public String testPage() {
        log.info("testPage");

        return "testPage";
    }

//    @GetMapping("/home")
//    public String home(@RequestHeader("Authorization") final String authorizationHeader, Model model) {
//        //헤더에서 액세스 토큰을 가져와 토큰 속 memberId를 꺼내 member를 찾아 home 화면에 사용자 이름을 반환
//        String accessToken = authorizationHeader.replace("Bearer ", "").trim();
//        log.info("accessToken={}", accessToken);
//
//        Long memberId = tokenProvider.getMemberId(accessToken);
//        Member findMember = memberService.findByMemberId(memberId);
//        model.addAttribute("memberName", findMember.getName());
//        return "home";
//    }
//@GetMapping("/home")
//public String home(Model model, Principal principal) {
//        log.info("principal={}", principal);
//    if (principal != null) {
//        String email = principal.getName();
//        String memberName = memberService.findByEmail(email).getName();
//        model.addAttribute("memberName", memberName);
//    }
//    return "home"; // home.html로 뷰 반환
//}



}
