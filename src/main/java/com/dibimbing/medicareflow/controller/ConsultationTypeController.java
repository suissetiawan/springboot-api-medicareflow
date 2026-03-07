package com.dibimbing.medicareflow.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

import com.dibimbing.medicareflow.dto.PaginationMeta;
import com.dibimbing.medicareflow.dto.request.ConsultationStatusRequest;
import com.dibimbing.medicareflow.dto.request.ConsultationTypeRequest;
import com.dibimbing.medicareflow.dto.response.ConsultationTypeResponse;
import com.dibimbing.medicareflow.helper.ResponseHelper;
import com.dibimbing.medicareflow.service.ConsultationTypeService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/consultationtypes")
@Tag(name = "Consultation Type Management", description = "Endpoints for managing various types of medical consultations.")
public class ConsultationTypeController {

    private final ConsultationTypeService consultationTypeService;

    @GetMapping
    public ResponseEntity<?> getAllConsultationTypes(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {

        Page<ConsultationTypeResponse> result = consultationTypeService.getAllConsultationTypes(pageable);

        PaginationMeta meta = PaginationMeta.builder()
                .page(result.getNumber() + 1)
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .build();

        return ResponseHelper.successOK(result.getContent(), "Data berhasil diambil", meta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getConsultationTypeById(@PathVariable Long id) {
        ConsultationTypeResponse data = consultationTypeService.getConsultationTypeById(id);
        return ResponseHelper.successOK(data, "Data berhasil diambil");
    }

    @GetMapping("/doctor/{username}")
    public ResponseEntity<?> getConsultationTypesByDoctorUsername(@PathVariable String username) {
        return ResponseHelper.successOK(consultationTypeService.getConsultationTypesByDoctorUsername(username), "Data berhasil diambil");
    }

    @PostMapping
    public ResponseEntity<?> createConsultationType(@Valid @RequestBody ConsultationTypeRequest request) {
        ConsultationTypeResponse data = consultationTypeService.createConsultationType(request);
        return ResponseHelper.successOK(data, "Data berhasil dibuat");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateConsultationType(@PathVariable Long id, @Valid @RequestBody ConsultationTypeRequest request) {
        ConsultationTypeResponse data = consultationTypeService.updateConsultationType(id, request);
        return ResponseHelper.successOK(data, "Data berhasil diupdate");
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @Valid @RequestBody ConsultationStatusRequest request) {
        ConsultationTypeResponse consultationType = consultationTypeService.updateStatus(id, request);
        return ResponseHelper.successOK(consultationType, "Data berhasil diupdate");
    }
}
