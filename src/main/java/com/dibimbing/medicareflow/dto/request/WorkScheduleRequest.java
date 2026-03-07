package com.dibimbing.medicareflow.dto.request;

import java.time.LocalTime;

import com.dibimbing.medicareflow.enums.DayOfWeek;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WorkScheduleRequest {
    @NotBlank(message = "Doctor username is required")
    private String usernameDoctor;
    
    @NotNull(message = "Day of week is required")
    private DayOfWeek dayOfWeek;
    
    @NotNull(message = "Start time is required")
    private LocalTime startTime;
    
    @NotNull(message = "End time is required")
    private LocalTime endTime;
}
