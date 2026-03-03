package com.dibimbing.medicareflow.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dibimbing.medicareflow.dto.PaginationMeta;

import com.dibimbing.medicareflow.dto.request.ConsultationRecordRequest;
import com.dibimbing.medicareflow.dto.response.ConsultationRecordResponse;
import com.dibimbing.medicareflow.helper.ResponseHelper;
import com.dibimbing.medicareflow.service.ConsultationRecordService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Consultation Record Management", description = "Endpoints for creating and retrieving medical consultation records.")
public class ConsultationRecordController {

    private final ConsultationRecordService consultationRecordService;

    @PostMapping("/appointments/{appointmentId}/records")
    public ResponseEntity<?> createRecord(
            @PathVariable Long appointmentId,
            @RequestBody ConsultationRecordRequest request) {
        ConsultationRecordResponse response = consultationRecordService.createRecord(appointmentId, request);
        return ResponseHelper.successCreated(response, "Successfully created consultation record");
    }

    @GetMapping("/appointments/{appointmentId}/records")
    public ResponseEntity<?> getRecordByAppointmentId(@PathVariable Long appointmentId) {
        ConsultationRecordResponse response = consultationRecordService.getRecordByAppointmentId(appointmentId);
        return ResponseHelper.successOK(response, "Successfully retrieved consultation record", null);
    }

    @GetMapping("/consultation-records/deleted")
    public ResponseEntity<?> getAllDeletedRecords(
            @PageableDefault(sort = "deleted_at", direction = Sort.Direction.DESC)
            Pageable pageable) {
        Page<ConsultationRecordResponse> result = consultationRecordService.getAllDeleted(pageable);
        
        PaginationMeta meta = PaginationMeta.builder()
                .page(result.getNumber() + 1)
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .build();

        return ResponseHelper.successOK(result.getContent(), "Successfully retrieved deleted consultation records", meta);
    }

    @PutMapping("/consultation-records/{id}/restore")
    public ResponseEntity<?> restore(@PathVariable Long id) {
        Boolean isRestored = consultationRecordService.restore(id);
        if (isRestored) {
            return ResponseEntity.noContent().build();
        }
        return ResponseHelper.error("Consultation record not found or already restored", org.springframework.http.HttpStatus.NOT_FOUND);
    }
}
