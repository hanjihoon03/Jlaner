package com.jlaner.project.service;

import com.jlaner.project.domain.Member;
import com.jlaner.project.domain.Post;
import com.jlaner.project.dto.PostDto;
import com.jlaner.project.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;

    public void savePost(PostDto postDto, Member member) {
        Post post = new Post(member, postDto.getContentData(), postDto.getScheduleDate());
        postRepository.save(post);
    }
    @Transactional(readOnly = true)
    public Post findByMemberId(Long memberId) {
        return postRepository.findByMemberId(memberId)
                .orElse(null);
    }
    @Transactional(readOnly = true)
    public Post findByScheduleDate(Date scheduleDate, Long memberId) {
        return postRepository.findByScheduleDateAndMemberId(scheduleDate, memberId)
                .orElse(null);
    }

    public void postDataSaveOrUpdate(PostDto postDto,Member member) {
        Post post = postRepository.findByScheduleDateAndMemberId(postDto.getScheduleDate(), member.getId())
                .orElse(null);
        if (post != null) {
            post.updateContentData(postDto.getContentData());
        } else {
            Post savePost = new Post(member, postDto.getContentData(), postDto.getScheduleDate());
            postRepository.save(savePost);
        }
    }

}
