package com.dibimbing.medicareflow.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsultationTypeRequest {
    private String name;
    private String fee;
    private String durationMinutes;
    private String isActive;
}
