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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dibimbing.medicareflow.dto.PaginationMeta;
import com.dibimbing.medicareflow.dto.request.AppointmentRequest;
import com.dibimbing.medicareflow.dto.response.AppointmentResponse;
import com.dibimbing.medicareflow.enums.AppointmentStatus;
import com.dibimbing.medicareflow.helper.ResponseHelper;
import com.dibimbing.medicareflow.service.AppointmentService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
@Tag(name = "Appointment Management", description = "Endpoints for booking, viewing, and updating medical appointments.")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<?> createAppointment(@RequestBody AppointmentRequest request) {
        AppointmentResponse response = appointmentService.createAppointment(request);
        return ResponseHelper.successCreated(response, "Successfully created appointment");
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyAppointments(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        Page<AppointmentResponse> result = appointmentService.getMyAppointments(pageable);
        
        PaginationMeta meta = PaginationMeta.builder()
                .page(result.getNumber() + 1)
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .build();

        return ResponseHelper.successOK(result.getContent(), "Successfully retrieved my appointments", meta);
    }

    @GetMapping
    public ResponseEntity<?> getAllAppointments(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        Page<AppointmentResponse> result = appointmentService.getAllAppointments(pageable);
        
        PaginationMeta meta = PaginationMeta.builder()
                .page(result.getNumber() + 1)
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .build();

        return ResponseHelper.successOK(result.getContent(), "Successfully retrieved all appointments", meta);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateAppointmentStatus(
            @PathVariable Long id,
            @RequestParam AppointmentStatus status) {
        AppointmentResponse response = appointmentService.updateAppointmentStatus(id, status);
        return ResponseHelper.successOK(response, "Successfully updated appointment status");
    }
}
