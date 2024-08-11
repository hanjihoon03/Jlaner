package com.jlaner.project.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;

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



}
