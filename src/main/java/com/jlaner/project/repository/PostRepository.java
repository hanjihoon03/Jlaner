package com.jlaner.project.repository;

import com.jlaner.project.domain.Post;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findByMemberId(Long memberId);
    Optional<Post> findByScheduleDate(Date scheduleDate);
    @Query("SELECT p FROM Post p WHERE p.scheduleDate = :scheduleDate AND p.member.id = :memberId")
    Optional<Post> findByScheduleDateAndMemberId(@Param("scheduleDate") Date scheduleDate, @Param("memberId") Long memberId);
}
