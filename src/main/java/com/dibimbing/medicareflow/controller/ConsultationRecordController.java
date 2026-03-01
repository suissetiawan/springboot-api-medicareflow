package com.dibimbing.medicareflow.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dibimbing.medicareflow.dto.request.ConsultationRecordRequest;
import com.dibimbing.medicareflow.dto.response.ConsultationRecordResponse;
import com.dibimbing.medicareflow.helper.ResponseHelper;
import com.dibimbing.medicareflow.service.ConsultationRecordService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/appointments/{appointmentId}/records")
@RequiredArgsConstructor
@Tag(name = "Consultation Record Management", description = "Endpoints for creating and retrieving medical consultation records.")
public class ConsultationRecordController {

    private final ConsultationRecordService consultationRecordService;

    @PostMapping
    public ResponseEntity<?> createRecord(
            @PathVariable UUID appointmentId,
            @RequestBody ConsultationRecordRequest request) {
        ConsultationRecordResponse response = consultationRecordService.createRecord(appointmentId, request);
        return ResponseHelper.successCreated(response, "Successfully created consultation record");
    }

    @GetMapping
    public ResponseEntity<?> getRecordByAppointmentId(@PathVariable UUID appointmentId) {
        ConsultationRecordResponse response = consultationRecordService.getRecordByAppointmentId(appointmentId);
        return ResponseHelper.successOK(response, "Successfully retrieved consultation record", null);
    }
}
