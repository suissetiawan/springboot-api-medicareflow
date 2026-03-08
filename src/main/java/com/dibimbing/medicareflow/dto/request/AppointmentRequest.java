package com.dibimbing.medicareflow.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequest {
    @NotNull(message = "Time slot ID is required")
    private Long timeSlotId;
    
    @NotNull(message = "Consultation type ID is required")
    private Long consultationTypeId;
}
