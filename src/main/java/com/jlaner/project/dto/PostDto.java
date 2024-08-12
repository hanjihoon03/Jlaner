package com.jlaner.project.dto;

import com.jlaner.project.domain.Member;
import com.jlaner.project.domain.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {

    private Member member;
    private String contentData;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date scheduleDate;

    public void setToPostDto(Post post) {
        this.contentData = post.getContentData();
        this.scheduleDate = post.getScheduleDate();
    }
}
