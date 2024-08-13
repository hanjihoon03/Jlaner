package com.jlaner.project.controller;


import com.jlaner.project.domain.Member;

import com.jlaner.project.domain.RefreshToken;

import com.jlaner.project.dto.PostDto;
import com.jlaner.project.dto.ScheduleAndPostDto;
import com.jlaner.project.dto.ScheduleDataDto;
import com.jlaner.project.service.MemberService;
import com.jlaner.project.service.PostService;
import com.jlaner.project.service.RefreshTokenRedisService;
import com.jlaner.project.service.ScheduleService;
import com.jlaner.project.util.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class JlanerController {

    private final RefreshTokenRedisService refreshTokenRedisService;
    private final MemberService memberService;
    private final PostService postService;
    private final ScheduleService scheduleService;


    @PostMapping("/jlaner/post/data")
    public ResponseEntity<?> savePostData(@RequestBody PostDto postDto,
                                          HttpServletRequest request,
                                          HttpServletResponse response
    ) {
        try {

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
        }catch (Exception e) {
            return ResponseEntity.status(401).build();
        }
    }

    @PostMapping("/jlaner/schedule/data")
    public ResponseEntity<?> saveScheduleData(@RequestBody ScheduleDataDto scheduleDataDto,
                                          HttpServletRequest request,
                                          HttpServletResponse response
    ) {
        try {
        String refreshToken = CookieUtil.getCookie(request, "refresh_token")
                .map(Cookie::getValue)
                .orElse(null);

        RefreshToken findByRefreshToken = refreshTokenRedisService.findByRefreshToken(refreshToken);
        Long findMemberId = findByRefreshToken.getMemberId();

        Member findMember = memberService.findByMemberId(findMemberId);

        scheduleService.scheduleDataSaveOrUpdate(scheduleDataDto, findMember);

        return ResponseEntity.status(200).build();

        } catch (Exception e) {
            return ResponseEntity.status(401).build();
        }
    }

    @GetMapping("/jlaner/data")
    public ResponseEntity<?> getMemberDateData(@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
                                               HttpServletRequest request) {

        try {

            String refreshToken = CookieUtil.getCookie(request, "refresh_token")
                    .map(Cookie::getValue)
                    .orElse(null);

            RefreshToken findByRefreshToken = refreshTokenRedisService.findByRefreshToken(refreshToken);
            Long findMemberId = findByRefreshToken.getMemberId();

            ScheduleDataDto findScheduleData = scheduleService.findByScheduleDate(date, findMemberId);
            PostDto findPostData = postService.findByPostDate(date, findMemberId);

            ScheduleAndPostDto sendData = new ScheduleAndPostDto(findScheduleData, findPostData);

            log.info("sendData={}", sendData.getPostData().getScheduleDate());
            log.info("sendData={}", sendData.getPostData().getContentData());
            log.info("sendData={}", sendData.getScheduleData().getScheduleDate());
            log.info("sendData={}", sendData.getScheduleData().getScheduleContent1());
            log.info("sendData={}", sendData.getScheduleData().isCheckBox1());

            return ResponseEntity.ok(sendData);
        } catch (Exception e) {
            return ResponseEntity.status(401).build();
        }


    }


}
