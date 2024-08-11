package com.jlaner.project.controller;

import com.jlaner.project.config.jwt.TokenProvider;
import com.jlaner.project.domain.Member;
import com.jlaner.project.domain.Post;
import com.jlaner.project.domain.RefreshToken;
import com.jlaner.project.domain.ScheduleData;
import com.jlaner.project.dto.PostDto;
import com.jlaner.project.service.MemberService;
import com.jlaner.project.service.PostService;
import com.jlaner.project.service.RefreshTokenRedisService;
import com.jlaner.project.util.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class JlanerController {

    private final RefreshTokenRedisService refreshTokenRedisService;
    private final MemberService memberService;
    private final PostService postService;


    @PostMapping("/jlaner/post/data")
    public ResponseEntity<?> savePostData(@RequestBody PostDto postDto,
                                          HttpServletRequest request,
                                          HttpServletResponse response
    ) {
        String refreshToken = CookieUtil.getCookie(request, "refresh_token")
                .map(Cookie::getValue)
                .orElse(null);

        RefreshToken findByRefreshToken = refreshTokenRedisService.findByRefreshToken(refreshToken);
        Long findMemberId = findByRefreshToken.getMemberId();

        Member findMember = memberService.findByMemberId(findMemberId);

        postService.postDataSaveOrUpdate(postDto,findMember);

        log.info("데이터가 저장되었습니다.{}", postDto.getContentData());
        log.info("date={}", postDto.getScheduleDate());

        return ResponseEntity.status(200).build();
    }

}
