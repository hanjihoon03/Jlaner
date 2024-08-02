package com.jlaner.project.repository;

import com.jlaner.project.domain.ScheduleData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<ScheduleData, Long> {
}
