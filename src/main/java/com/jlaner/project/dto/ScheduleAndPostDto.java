package com.jlaner.project.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ScheduleAndPostDto {
    private ScheduleDataDto scheduleData;
    private PostDto postData;

}
