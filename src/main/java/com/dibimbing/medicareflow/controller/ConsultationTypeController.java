package com.dibimbing.medicareflow.controller;

import java.util.List;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dibimbing.medicareflow.dto.request.ConsultationTypeRequest;
import com.dibimbing.medicareflow.dto.response.ConsultationTypeResponse;
import com.dibimbing.medicareflow.helper.ResponseHelper;
import com.dibimbing.medicareflow.service.ConsultationTypeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/consultationtypes")
public class ConsultationTypeController {

    private final ConsultationTypeService consultationTypeService;

    @GetMapping
    public ResponseEntity<?> getAllConsultationTypes() {
        List<ConsultationTypeResponse> data = consultationTypeService.getAllConsultationTypes();
        return ResponseHelper.successOK(data, "Data berhasil diambil");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getConsultationTypeById(@PathVariable Long id) {
        ConsultationTypeResponse data = consultationTypeService.getConsultationTypeById(id);
        return ResponseHelper.successOK(data, "Data berhasil diambil");
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody ConsultationTypeRequest request) {
        ConsultationTypeResponse consultationType = consultationTypeService.updateStatus(id, request);
        return ResponseHelper.successOK(consultationType, "Data berhasil diupdate");
    }
}
