package com.dibimbing.medicareflow.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse {
    private String id;
    private String doctorName;
    private String patientName;
    private String consultationType;
    private String status;
    private String appointmentDate;
    private String startTime;
    private String endTime;
    private String bookedAt;
}
