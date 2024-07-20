//package com.jlaner.project.service;
//
//import com.jlaner.project.domain.User;
//import com.jlaner.project.dto.UserDataDto;
//import com.jlaner.project.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//@RequiredArgsConstructor
//@Service
//public class UserService {
//    private final UserRepository userRepository;
//
//    public Long save(UserDataDto dto) {
//        return userRepository.save(User.builder()
//                .name(dto.getName())
//                .email(dto.getEmail())
//                .phoneNumber(dto.getPhoneNumber())
//                .build()).getId();
//    }
//
//    public User findById(Long userId) {
//        return userRepository.findById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
//    }
//
//    public User findByEmail(String email) {
//        return userRepository.findByEmail(email)
//                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
//    }
//}
