package com.dibimbing.medicareflow.dto.request;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationRecordRequest {
    private String summary;
    private String recommendation;
    private LocalDate followUpDate;
}
