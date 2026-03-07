package com.dibimbing.medicareflow.dto.request;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationRecordRequest {
    @NotBlank(message = "Summary is required")
    private String summary;
    
    @NotBlank(message = "Recommendation is required")
    private String recommendation;
    private LocalDate followUpDate;
}
