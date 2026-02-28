package com.dibimbing.medicareflow.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.dibimbing.medicareflow.dto.response.PatientDetailResponse;
import com.dibimbing.medicareflow.entity.Patient;
import com.dibimbing.medicareflow.entity.UserAccount;
import com.dibimbing.medicareflow.exception.NotFoundException;
import com.dibimbing.medicareflow.helper.DateHelper;
import com.dibimbing.medicareflow.repository.PatientRepository;
import com.dibimbing.medicareflow.repository.UserAccountRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientService {

    private final PatientRepository patientRepository;
    private final UserAccountRepository userAccountRepository;

    public PatientDetailResponse getPatientById(UUID id) {
        Patient patient = patientRepository.findById(id).orElseThrow(() -> {
            log.warn("Patient not found");
            return new NotFoundException("Patient not found");
        });

        PatientDetailResponse res = new PatientDetailResponse();
        res.setId(patient.getId().toString());
        res.setName(patient.getName());
        res.setUsername(patient.getUserAccount().getUsername());
        res.setEmail(patient.getUserAccount().getEmail());
        res.setPhone(patient.getPhone());
        res.setRole(patient.getUserAccount().getRole().toString());
        res.setCreatedAt(DateHelper.format(patient.getCreatedAt()));
        res.setUpdatedAt(DateHelper.format(patient.getUpdatedAt()));
        
        return res;
    }

    @Transactional
    public Boolean deletePatient(UUID id) {
        Patient patient = patientRepository.findById(id).orElseThrow(() -> {
            log.warn("Patient has been deleted");
            return new NotFoundException("Patient has been deleted");
        });

        if (patient.getDeletedAt() != null) {
            return false; // idempotent
        }

        patient.setDeletedAt(LocalDateTime.now());
        patientRepository.save(patient);
        
        UserAccount account = patient.getUserAccount();
        account.setDeletedAt(LocalDateTime.now());
        userAccountRepository.save(account);

        return true;
    }

}
