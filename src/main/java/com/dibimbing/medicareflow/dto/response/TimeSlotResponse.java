package com.dibimbing.medicareflow.dto.response;

import lombok.Data;
import java.io.Serializable;

@Data
public class TimeSlotResponse implements Serializable {
    private String id;
    private String doctorUsername;
    private String slotDate;
    private String startTime;
    private String endTime;
    private String status;
    private String dayOfWeek;
}
