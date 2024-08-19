//package com.jlaner.project.controller;
//
//import com.jlaner.project.config.jwt.TokenProvider;
//import com.jlaner.project.domain.Member;
//import com.jlaner.project.domain.Post;
//import com.jlaner.project.dto.PostDto;
//import com.jlaner.project.service.MemberService;
//import com.jlaner.project.service.PostService;
//import com.jlaner.project.service.RefreshTokenRedisService;
//import lombok.extern.slf4j.Slf4j;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.security.web.FilterChainProxy;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@Slf4j
//public class JlanerControllerTest {
//    @Autowired
//    private WebApplicationContext webApplicationContext;
//
//    private MockMvc mockMvc;
//
//    @MockBean
//    private TokenProvider tokenProvider;
//
//
//    @MockBean
//    private PostService postService;
//
//    @MockBean
//    private MemberService memberService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
//                .addFilter((FilterChainProxy) webApplicationContext.getBean("springSecurityFilterChain"))
//                .build();
//
//    }
//
//    @DisplayName("post 데이터를 저장하는 컨트롤러의 테스트")
//    @Test
//    @WithMockUser
//    void postDataSaveTest() throws Exception {
//        //given
//        String email = "test@test.com";
//        String accessToken = "accessToken";
//        String contentData = "테스트 데이터입니다.";
//        Date testDate = new Date();
//
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//
//        Member member = new Member();
//        member.setId(1L);
//        member.setEmail(email);
//
//
//        PostDto postDto = new PostDto(member,contentData,testDate);
//
//        //when
//        when(tokenProvider.validToken(accessToken)).thenReturn(true);
//        when(tokenProvider.getAuthentication(accessToken)).thenReturn(mock(Authentication.class));
//        when(memberService.findByMemberId(1L)).thenReturn(member);
//
//        Post mockPost = new Post(member, contentData, testDate);
//        when(postService.findByMemberId(1L)).thenReturn(mockPost);
//
//        mockMvc.perform(post("/api/jlaner/post/data")
//                .header("Authorization", "Bearer " + accessToken)
//                                .contentType("application/json")
//                                .content("{\"contentData\":\"테스트 데이터입니다.\", \"scheduleDate\":\"2024-08-15\"}")  // JSON 데이터
//                                .with(csrf()))
//                .andExpect(status().is3xxRedirection());
//
//        Post findPost = postService.findByMemberId(member.getId());
//
//
//        assertThat(findPost.getContentData()).isEqualTo(postDto.getContentData());
//        assertThat(findPost.getScheduleDate()).isEqualTo(postDto.getScheduleDate());
//
//
//    }
//}
