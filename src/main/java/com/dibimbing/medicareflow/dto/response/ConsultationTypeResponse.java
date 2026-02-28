package com.dibimbing.medicareflow.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsultationTypeResponse {
    private String id;
    private String name;
    private String fee;
    private String durationMinutes;
    private String isActive;
    private String updatedAt;
    private String createdAt;
}
