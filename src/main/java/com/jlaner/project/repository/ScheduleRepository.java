package com.jlaner.project.repository;

import com.jlaner.project.domain.ScheduleData;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<ScheduleData, Long> {
    List<ScheduleData> findByMemberId(Long memberId);


    @Query("SELECT s FROM ScheduleData s WHERE s.scheduleDate = :scheduleDate AND s.member.id = :memberId")
    Optional<ScheduleData> findByScheduleDataScheduleDateAndMemberId(@Param("scheduleDate") Date scheduleDate, @Param("memberId") Long memberId);
}
