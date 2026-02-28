package com.dibimbing.medicareflow.dto.request;

import java.time.LocalTime;

import com.dibimbing.medicareflow.enums.DayOfWeek;

import lombok.Data;

@Data
public class WorkScheduleRequest {
    private String usernameDoctor;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
}
