package com.jlaner.project.service;

import com.jlaner.project.domain.Member;
import com.jlaner.project.domain.ScheduleData;
import com.jlaner.project.dto.ScheduleDataDto;
import com.jlaner.project.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public void saveScheduleData(ScheduleDataDto scheduleDataDto, Member member) {
        ScheduleData scheduleData = new ScheduleData(
                member,
                scheduleDataDto.getScheduleDate(),
                scheduleDataDto.isCheckBox1(),
                scheduleDataDto.isCheckBox2(),
                scheduleDataDto.isCheckBox3(),
                scheduleDataDto.isCheckBox4(),
                scheduleDataDto.isCheckBox5(),
                scheduleDataDto.isCheckBox6(),
                scheduleDataDto.isCheckBox7(),
                scheduleDataDto.isCheckBox8(),
                scheduleDataDto.isCheckBox9(),
                scheduleDataDto.isCheckBox10(),
                scheduleDataDto.isCheckBox11(),
                scheduleDataDto.isCheckBox12(),
                scheduleDataDto.getScheduleContent1(),
                scheduleDataDto.getScheduleContent2(),
                scheduleDataDto.getScheduleContent3(),
                scheduleDataDto.getScheduleContent4(),
                scheduleDataDto.getScheduleContent5(),
                scheduleDataDto.getScheduleContent6(),
                scheduleDataDto.getScheduleContent7(),
                scheduleDataDto.getScheduleContent8(),
                scheduleDataDto.getScheduleContent9(),
                scheduleDataDto.getScheduleContent10(),
                scheduleDataDto.getScheduleContent11(),
                scheduleDataDto.getScheduleContent12()
        );
        scheduleRepository.save(scheduleData);
    }

    @Transactional
    public void scheduleDataSaveOrUpdate(ScheduleDataDto scheduleDataDto, Member member) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String stringDate = simpleDateFormat.format(scheduleDataDto.getScheduleDate());
            Date newDate = simpleDateFormat.parse(stringDate);
            scheduleDataDto.setScheduleDate(newDate);

        ScheduleData scheduleData = scheduleRepository.findByScheduleDataScheduleDateAndMemberId(scheduleDataDto.getScheduleDate(), member.getId())
                .orElse(null);
        if (scheduleData != null) {
            scheduleData.updateScheduleData(scheduleDataDto);
        } else {
            ScheduleData saveScheduleData = new ScheduleData(
                    member,
                    scheduleDataDto.getScheduleDate(),
                    scheduleDataDto.isCheckBox1(),
                    scheduleDataDto.isCheckBox2(),
                    scheduleDataDto.isCheckBox3(),
                    scheduleDataDto.isCheckBox4(),
                    scheduleDataDto.isCheckBox5(),
                    scheduleDataDto.isCheckBox6(),
                    scheduleDataDto.isCheckBox7(),
                    scheduleDataDto.isCheckBox8(),
                    scheduleDataDto.isCheckBox9(),
                    scheduleDataDto.isCheckBox10(),
                    scheduleDataDto.isCheckBox11(),
                    scheduleDataDto.isCheckBox12(),
                    scheduleDataDto.getScheduleContent1(),
                    scheduleDataDto.getScheduleContent2(),
                    scheduleDataDto.getScheduleContent3(),
                    scheduleDataDto.getScheduleContent4(),
                    scheduleDataDto.getScheduleContent5(),
                    scheduleDataDto.getScheduleContent6(),
                    scheduleDataDto.getScheduleContent7(),
                    scheduleDataDto.getScheduleContent8(),
                    scheduleDataDto.getScheduleContent9(),
                    scheduleDataDto.getScheduleContent10(),
                    scheduleDataDto.getScheduleContent11(),
                    scheduleDataDto.getScheduleContent12()
            );
            scheduleRepository.save(saveScheduleData);
        }


        }catch (ParseException e) {
            log.error("parsing error", e);
        }


    }

    public ScheduleDataDto findByScheduleDate(Date scheduleDate, Long memberId) {
        ScheduleData scheduleData = scheduleRepository.findByScheduleDataScheduleDateAndMemberId(scheduleDate, memberId)
                .orElse(null);

        if (scheduleData != null) {
            ScheduleDataDto findScheduleData = new ScheduleDataDto();
            findScheduleData.setToScheduleDataDto(scheduleData);
            return findScheduleData;
        } else {
            return new ScheduleDataDto();
        }
    }




}
