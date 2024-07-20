//package com.jlaner.project.security;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultMatcher;
//
//import static org.hamcrest.Matchers.containsStringIgnoringCase;
//import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@AutoConfigureMockMvc
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//public class WebSecurityConfigTest {
//
//    @Autowired
//    private MockMvc api;
//
//    @Test
//    void anyoneCanViewPublicEndpoint() throws Exception {
//        api.perform(get("/login"))
//                .andExpect(status().isOk())
//                .andExpect((ResultMatcher) content().string(containsStringIgnoringCase("Hello")));
//    }
//
//}
