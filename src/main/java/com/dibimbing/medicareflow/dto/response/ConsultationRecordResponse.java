package com.dibimbing.medicareflow.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationRecordResponse {
    private String id;
    private String appointmentId;
    private String summary;
    private String recommendation;
    private String followUpDate;
}
