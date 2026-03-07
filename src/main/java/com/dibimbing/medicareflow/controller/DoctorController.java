package com.dibimbing.medicareflow.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dibimbing.medicareflow.helper.ResponseHelper;
import com.dibimbing.medicareflow.service.DoctorService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
@Tag(name = "Doctor Management", description = "Endpoints for managing doctors and their assigned services.")
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping("/{username}/services/{serviceId}")
    public ResponseEntity<?> addService(@PathVariable String username, @PathVariable Long serviceId) {
        String message = doctorService.addService(username, serviceId);
        return ResponseHelper.successOK(null, message);
    }

    @DeleteMapping("/{username}/services/{serviceId}")
    public ResponseEntity<?> removeService(@PathVariable String username, @PathVariable Long serviceId) {
        String message = doctorService.removeService(username, serviceId);
        return ResponseHelper.successOK(null, message);
    }
}
