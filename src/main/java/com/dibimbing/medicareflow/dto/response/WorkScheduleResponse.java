package com.dibimbing.medicareflow.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

import lombok.Data;

@Data
@JsonPropertyOrder({
    "id",
    "doctorUsername",
    "dayOfWeek",
    "startTime",
    "endTime"
})
public class WorkScheduleResponse implements Serializable {
    private String id;
    private String doctorUsername;
    private String dayOfWeek;
    private String startTime;
    private String endTime;
}
