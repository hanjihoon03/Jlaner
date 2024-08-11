package com.jlaner.project.service;

import com.jlaner.project.domain.ScheduleData;
import com.jlaner.project.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public void saveScheduleData(ScheduleData scheduleData) {
        scheduleRepository.save(scheduleData);
    }
}
