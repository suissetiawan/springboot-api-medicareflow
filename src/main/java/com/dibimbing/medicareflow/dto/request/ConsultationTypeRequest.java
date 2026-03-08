package com.dibimbing.medicareflow.dto.request;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationTypeRequest {
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotNull(message = "Fee is required")
    @PositiveOrZero(message = "Fee must be zero or positive")
    private BigDecimal fee;
    
    @NotNull(message = "Duration is required")
    @Positive(message = "Duration must be positive")
    private Integer durationMinutes;
    
    @NotNull(message = "isActive is required")
    private Boolean isActive;
}
