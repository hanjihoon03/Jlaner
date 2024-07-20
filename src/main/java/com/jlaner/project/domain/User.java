//package com.jlaner.project.domain;
//
//import jakarta.persistence.*;
//import lombok.AccessLevel;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//
//@Table(name = "users")
//@Getter
//@Entity
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//public class User{
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    private String name;
//    @Column(name = "email", nullable = false, unique = true)
//    private String email;
//    private String phoneNumber;
//    // provider : google이 들어감
//    private String provider;
//
//    // providerId : 구굴 로그인 한 유저의 고유 ID가 들어감
//    private String providerId;
//
//    @Builder
//    public User(String name, String email, String phoneNumber) {
//        this.name = name;
//        this.email = email;
//        this.phoneNumber = phoneNumber;
//    }
//
//    public User update(String name) {
//        this.name = name;
//
//        return this;
//    }
//
//}
