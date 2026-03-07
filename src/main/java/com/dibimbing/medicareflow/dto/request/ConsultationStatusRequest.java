package com.dibimbing.medicareflow.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsultationStatusRequest {
    @NotBlank(message = "isActive is required")
    private String isActive;
}
