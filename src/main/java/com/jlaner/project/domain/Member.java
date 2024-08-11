package com.jlaner.project.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    private String loginId;
    private String name;

    private String email;

    @OneToMany(mappedBy = "member")
    @Builder.Default
    private List<ScheduleData> scheduleDataList = new ArrayList<>();
    @OneToMany(mappedBy = "member")
    @Builder.Default
    private List<Post> postList = new ArrayList<>();


    @Enumerated(EnumType.STRING)
    private MemberRole role;

    private String provider;
    private String providerId;

    public Member(String loginId, String name, String email, MemberRole role) {
        this.loginId = loginId;
        this.name = name;
        this.email = email;
        this.role = role;
    }
}
