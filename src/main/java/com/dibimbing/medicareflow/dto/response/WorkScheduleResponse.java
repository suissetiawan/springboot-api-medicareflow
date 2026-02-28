package com.dibimbing.medicareflow.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
@JsonPropertyOrder({
    "id",
    "doctorUsername",
    "dayOfWeek",
    "startTime",
    "endTime"
})
public class WorkScheduleResponse {
    private String id;
    private String doctorUsername;
    private String dayOfWeek;
    private String startTime;
    private String endTime;
}
