//package com.jlaner.project.config.outh2;
//
//import com.jlaner.project.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//
//
//import com.jlaner.project.domain.User;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Service;
//
//import java.util.Map;
//
///**
// * OAuth2UserCustomService는 OAuth2 로그인 과정에서 사용자 정보를 가져오고,
// * 데이터베이스에 사용자를 저장하거나 업데이트하는 서비스를 제공합니다.
// */
//@RequiredArgsConstructor
//@Service
//@Slf4j
//public class OAuth2UserCustomService extends DefaultOAuth2UserService {
//
//    // UserRepository를 주입받습니다.
//    private final UserRepository userRepository;
//
//    /**
//     * OAuth2 사용자 정보를 로드합니다.
//     *
//     * @param userRequest OAuth2UserRequest 객체
//     * @return OAuth2User 객체
//     * @throws OAuth2AuthenticationException OAuth2 인증 예외 발생 시
//     */
//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//        // 기본 OAuth2UserService를 사용하여 사용자 정보를 로드합니다.
//        OAuth2User user = super.loadUser(userRequest);
//        // 사용자 정보를 데이터베이스에 저장하거나 업데이트합니다.
//        saveOrUpdate(user);
//
//        return user;
//    }
//
//    /**
//     * OAuth2 사용자 정보를 데이터베이스에 저장하거나 업데이트합니다.
//     *
//     * @param oAuth2User OAuth2 사용자 정보
//     * @return 저장된 또는 업데이트된 User 객체
//     */
//    private User saveOrUpdate(OAuth2User oAuth2User) {
//        // OAuth2 사용자 정보에서 속성을 가져옵니다.
//        Map<String, Object> attributes = oAuth2User.getAttributes();
//
//        // 이메일, 이름, 전화번호를 가져옵니다.
//        String email = (String) attributes.get("email");
//        String name = (String) attributes.get("name");
//        String phoneNumber = (String) attributes.get("phoneNumber");
//        log.info("email={}", email);
//        log.info("name={}", name);
//        log.info("phoneNumber={}", phoneNumber);
//
//        // 이메일을 기준으로 사용자를 찾고, 존재하면 이름을 업데이트합니다.
//        // 존재하지 않으면 새로운 사용자 객체를 생성합니다.
//        User user = userRepository.findByEmail(email)
//                .map(entity -> entity.update(name))
//                .orElse(User.builder()
//                        .email(email)
//                        .name(name)
//                        .phoneNumber(phoneNumber)
//                        .build());
//
//        // 사용자 정보를 데이터베이스에 저장합니다.
//        return userRepository.save(user);
//    }
//}