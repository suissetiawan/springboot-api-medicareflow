package com.dibimbing.medicareflow.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dibimbing.medicareflow.helper.ResponseHelper;
import com.dibimbing.medicareflow.service.PatientService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/patients")
public class PatientController {

    private final PatientService patientService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getPatientById(@PathVariable UUID id) {
        return ResponseHelper.successOK(patientService.getPatientById(id), "Success get detail data patient");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePatient(@PathVariable UUID id) {
        Boolean isDeleted = patientService.deletePatient(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        }

        return ResponseHelper.error("Data has been deleted", HttpStatus.BAD_REQUEST);
    }
}
