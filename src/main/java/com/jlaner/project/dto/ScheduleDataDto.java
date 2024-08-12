package com.jlaner.project.dto;

import com.jlaner.project.domain.Member;
import com.jlaner.project.domain.ScheduleData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleDataDto {
    private Member member;


    private Date scheduleDate;

    private boolean checkBox1;
    private boolean checkBox2;
    private boolean checkBox3;
    private boolean checkBox4;
    private boolean checkBox5;
    private boolean checkBox6;
    private boolean checkBox7;
    private boolean checkBox8;
    private boolean checkBox9;
    private boolean checkBox10;
    private boolean checkBox11;
    private boolean checkBox12;


    private String scheduleContent1;
    private String scheduleContent2;
    private String scheduleContent3;
    private String scheduleContent4;
    private String scheduleContent5;
    private String scheduleContent6;
    private String scheduleContent7;
    private String scheduleContent8;
    private String scheduleContent9;
    private String scheduleContent10;
    private String scheduleContent11;
    private String scheduleContent12;

    public void setToScheduleDataDto(ScheduleData scheduleData) {
        this.scheduleDate = scheduleData.getScheduleDate();
        this.checkBox1 = scheduleData.isCheckBox1();
        this.checkBox2 = scheduleData.isCheckBox2();
        this.checkBox3 = scheduleData.isCheckBox3();
        this.checkBox4 = scheduleData.isCheckBox4();
        this.checkBox5 = scheduleData.isCheckBox5();
        this.checkBox6 = scheduleData.isCheckBox6();
        this.checkBox7 = scheduleData.isCheckBox7();
        this.checkBox8 = scheduleData.isCheckBox8();
        this.checkBox9 = scheduleData.isCheckBox9();
        this.checkBox10 = scheduleData.isCheckBox10();
        this.checkBox11 = scheduleData.isCheckBox11();
        this.checkBox12 = scheduleData.isCheckBox12();
        this.scheduleContent1 = scheduleData.getScheduleContent1();
        this.scheduleContent2 = scheduleData.getScheduleContent2();
        this.scheduleContent3 = scheduleData.getScheduleContent3();
        this.scheduleContent4 = scheduleData.getScheduleContent4();
        this.scheduleContent5 = scheduleData.getScheduleContent5();
        this.scheduleContent6 = scheduleData.getScheduleContent6();
        this.scheduleContent7 = scheduleData.getScheduleContent7();
        this.scheduleContent8 = scheduleData.getScheduleContent8();
        this.scheduleContent9 = scheduleData.getScheduleContent9();
        this.scheduleContent10 = scheduleData.getScheduleContent10();
        this.scheduleContent11 = scheduleData.getScheduleContent11();
        this.scheduleContent12 = scheduleData.getScheduleContent12();
    }
}
