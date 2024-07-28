//package com.jlaner.project.Token;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.jlaner.project.config.jwt.JwtProperties;
//import com.jlaner.project.domain.Member;
//import com.jlaner.project.domain.MemberRole;
//import com.jlaner.project.domain.RefreshToken;
//import com.jlaner.project.repository.MemberRepository;
//import com.jlaner.project.repository.RefreshTokenRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.util.Optional;
//
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@Slf4j
//public class TestRedisDelete {
//    @Autowired
//    protected MockMvc mockMvc;
//
//    @Autowired
//    protected ObjectMapper objectMapper;
//
//    @Autowired
//    private WebApplicationContext context;
//
//    @Autowired
//    JwtProperties jwtProperties;
//
//    @Autowired
//    MemberRepository memberRepository;
//
//    @Autowired
//    RefreshTokenRepository refreshTokenRepository;
//
//    @BeforeEach
//    public void mockMvcSetUp() {
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
//        refreshTokenRepository.deleteAll();
//        memberRepository.deleteAll();
//    }
//
//    @DisplayName("리프레시 토큰을 이용하여 레디스에서 토큰 정보 삭제")
//    @Test
//    void removeRefreshToken() throws Exception {
//        // given
//        final String url = "/token/logout";
//
//        Member member = new Member("test", "1234", "tester", MemberRole.USER);
//        memberRepository.save(member);
//
//        String accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJ0ZXN0IiwiaWF0IjoxNzIxNjM3NzQ4LCJleHAiOjE3MjE3MjQxNDgsInN1YiI6Iu2VnOyngO2biCIsImlkIjoxfQ.YG38d-X6DgBJ_EVcJSZ31FdawHvNmlbGaJQ0km-iNxc";
//        String refreshToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJ0ZXN0IiwiaWF0IjoxNzIxNjM3NzQ4LCJleHAiOjE3MjI4NDczNDgsInN1YiI6Iu2VnOyngO2biCIsImlkIjoxfQ.0sy6zTUyyH_Vt-RCp2GN61AJbURABuush2tNLuRaTdo";
//
//        RefreshToken refreshTokenEntity = new RefreshToken(member.getId(), refreshToken, accessToken);
//        refreshTokenRepository.save(refreshTokenEntity);
//
//        // when
//        ResultActions resultActions = mockMvc.perform(post(url)
//                .header("Authorization", accessToken)
//                .contentType(MediaType.APPLICATION_JSON_VALUE));
//
//        // then
//        resultActions.andExpect(status().isOk());
//
//        Optional<RefreshToken> deletedToken = refreshTokenRepository.findByAccessToken(accessToken);
//        Assertions.assertThat(deletedToken).isEmpty();
//    }
//}
