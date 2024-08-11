package com.jlaner.project.repository;

import com.jlaner.project.domain.ScheduleData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<ScheduleData, Long> {
}
