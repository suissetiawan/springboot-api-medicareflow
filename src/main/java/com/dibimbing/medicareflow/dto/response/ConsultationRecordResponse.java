package com.dibimbing.medicareflow.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConsultationRecordResponse implements Serializable {
    private Long id;
    private Long appointmentId;
    private String summary;
    private String recommendation;
    private String followUpDate;
    private String doctorUsername;
    private String patientUsername;
}
