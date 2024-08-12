package com.jlaner.project.domain;

import com.jlaner.project.dto.ScheduleDataDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleData {

    @Id
    @GeneratedValue
    @Column(name = "scheduleData_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(unique = true)
    private Date scheduleDate;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
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

    public ScheduleData(Member member, Date scheduleDate, boolean checkBox1, boolean checkBox2, boolean checkBox3, boolean checkBox4, boolean checkBox5, boolean checkBox6, boolean checkBox7, boolean checkBox8, boolean checkBox9, boolean checkBox10, boolean checkBox11, boolean checkBox12, String scheduleContent1, String scheduleContent2, String scheduleContent3, String scheduleContent4, String scheduleContent5, String scheduleContent6, String scheduleContent7, String scheduleContent8, String scheduleContent9, String scheduleContent10, String scheduleContent11, String scheduleContent12) {
        this.member = member;
        this.scheduleDate = scheduleDate;
        this.checkBox1 = checkBox1;
        this.checkBox2 = checkBox2;
        this.checkBox3 = checkBox3;
        this.checkBox4 = checkBox4;
        this.checkBox5 = checkBox5;
        this.checkBox6 = checkBox6;
        this.checkBox7 = checkBox7;
        this.checkBox8 = checkBox8;
        this.checkBox9 = checkBox9;
        this.checkBox10 = checkBox10;
        this.checkBox11 = checkBox11;
        this.checkBox12 = checkBox12;
        this.scheduleContent1 = scheduleContent1;
        this.scheduleContent2 = scheduleContent2;
        this.scheduleContent3 = scheduleContent3;
        this.scheduleContent4 = scheduleContent4;
        this.scheduleContent5 = scheduleContent5;
        this.scheduleContent6 = scheduleContent6;
        this.scheduleContent7 = scheduleContent7;
        this.scheduleContent8 = scheduleContent8;
        this.scheduleContent9 = scheduleContent9;
        this.scheduleContent10 = scheduleContent10;
        this.scheduleContent11 = scheduleContent11;
        this.scheduleContent12 = scheduleContent12;
    }

    public void updateScheduleData(ScheduleDataDto scheduleDataDto) {
        this.checkBox1 = scheduleDataDto.isCheckBox1();
        this.checkBox2 = scheduleDataDto.isCheckBox2();
        this.checkBox3 = scheduleDataDto.isCheckBox3();
        this.checkBox4 = scheduleDataDto.isCheckBox4();
        this.checkBox5 = scheduleDataDto.isCheckBox5();
        this.checkBox6 = scheduleDataDto.isCheckBox6();
        this.checkBox7 = scheduleDataDto.isCheckBox7();
        this.checkBox8 = scheduleDataDto.isCheckBox8();
        this.checkBox9 = scheduleDataDto.isCheckBox9();
        this.checkBox10 = scheduleDataDto.isCheckBox10();
        this.checkBox11 = scheduleDataDto.isCheckBox11();
        this.checkBox12 = scheduleDataDto.isCheckBox12();
        this.scheduleContent1 = scheduleDataDto.getScheduleContent1();
        this.scheduleContent2 = scheduleDataDto.getScheduleContent2();
        this.scheduleContent3 = scheduleDataDto.getScheduleContent3();
        this.scheduleContent4 = scheduleDataDto.getScheduleContent4();
        this.scheduleContent5 = scheduleDataDto.getScheduleContent5();
        this.scheduleContent6 = scheduleDataDto.getScheduleContent6();
        this.scheduleContent7 = scheduleDataDto.getScheduleContent7();
        this.scheduleContent8 = scheduleDataDto.getScheduleContent8();
        this.scheduleContent9 = scheduleDataDto.getScheduleContent9();
        this.scheduleContent10 = scheduleDataDto.getScheduleContent10();
        this.scheduleContent11 = scheduleDataDto.getScheduleContent11();
        this.scheduleContent12 = scheduleDataDto.getScheduleContent12();
    }
}
