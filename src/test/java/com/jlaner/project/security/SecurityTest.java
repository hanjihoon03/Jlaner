//package com.jlaner.project.security;
//
//import com.jlaner.project.config.jwt.TokenProvider;
//import com.jlaner.project.config.outh2.OAuth2SuccessHandler;
//import com.jlaner.project.domain.Member;
//import com.jlaner.project.domain.RefreshToken;
//import com.jlaner.project.service.MemberService;
//import com.jlaner.project.service.RefreshTokenRedisService;
//import com.jlaner.project.util.CookieUtil;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
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
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.time.Duration;
//import java.util.Optional;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.mockito.Mockito.*;
//
//
//@SpringBootTest
//public class SecurityTest {
//
//    @Autowired
//    private WebApplicationContext webApplicationContext;
//
//    private MockMvc mockMvc;
//
//    @MockBean
//    private TokenProvider tokenProvider;
//
//    @MockBean
//    private RefreshTokenRedisService refreshTokenRedisService;
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
//    @DisplayName("유효한 토큰을 사용해 홈으로 접근하는 테스트")
//    @Test
//    @WithMockUser
//    void homeEndPointWithValidToken() throws Exception {
//        //given
//        String email = "test@test.com";
//        String accessToken = "accessToken";
//
//        Member member = new Member();
//        member.setId(1L);
//        member.setEmail(email);
//
//        //when
//        when(tokenProvider.validToken(accessToken)).thenReturn(true);
//        when(tokenProvider.getAuthentication(accessToken)).thenReturn(mock(Authentication.class));
//
//        mockMvc.perform(get("/home")
//                .header("Authorization", "Bearer " + accessToken))
//                .andExpect(status().is3xxRedirection());
//
//        //then
//        verify(tokenProvider, times(1)).validToken(accessToken);
//        verify(tokenProvider, times(1)).getAuthentication(accessToken);
//
//    }
//
//
//
//}
