package com.jlaner.project.domain;

import com.jlaner.project.dto.PostDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String contentData;
    @Column(unique = true)
    private Date scheduleDate;

    public Post(Member member, String contentData, Date scheduleDate) {
        this.member = member;
        this.contentData = contentData;
        this.scheduleDate = scheduleDate;
    }

    public void updateContentData(String newContentData) {
        this.contentData = newContentData;
    }

}
