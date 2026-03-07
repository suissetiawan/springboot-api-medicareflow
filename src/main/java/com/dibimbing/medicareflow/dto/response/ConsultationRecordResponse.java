package com.dibimbing.medicareflow.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationRecordResponse implements Serializable {
    private Long id;
    private Long appointmentId;
    private String summary;
    private String recommendation;
    private String followUpDate;
}
