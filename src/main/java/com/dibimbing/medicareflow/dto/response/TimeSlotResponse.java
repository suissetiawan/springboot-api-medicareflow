package com.dibimbing.medicareflow.dto.response;

import lombok.Data;

@Data
public class TimeSlotResponse {
    private String id;
    private String doctorUsername;
    private String slotDate;
    private String startTime;
    private String endTime;
    private String status;
}
