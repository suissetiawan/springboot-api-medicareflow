package com.dibimbing.medicareflow.dto.request;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationTypeRequest {
    private String name;
    private BigDecimal fee;
    private Integer durationMinutes;
    private Boolean isActive;
}
