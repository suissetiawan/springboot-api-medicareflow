package com.dibimbing.medicareflow.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.dibimbing.medicareflow.dto.response.DoctorDetailResponse;
import com.dibimbing.medicareflow.entity.Doctor;
import com.dibimbing.medicareflow.entity.UserAccount;
import com.dibimbing.medicareflow.exception.NotFoundException;
import com.dibimbing.medicareflow.repository.DoctorRepository;
import com.dibimbing.medicareflow.repository.UserAccountRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserAccountRepository userAccountRepository;

    public DoctorDetailResponse getDoctorById(UUID id) {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> {
            log.warn("Doctor not found");
            return new NotFoundException("Doctor not found");
        });

        DoctorDetailResponse res = new DoctorDetailResponse();
        res.setId(doctor.getId().toString());
        res.setName(doctor.getName());
        res.setUsername(doctor.getUserAccount().getUsername());
        res.setEmail(doctor.getUserAccount().getEmail());
        res.setSpecialization(doctor.getSpecialization());
        res.setRole(doctor.getUserAccount().getRole().toString());
        res.setCreatedAt(doctor.getCreatedAt().toString());
        res.setUpdatedAt(doctor.getUpdatedAt().toString());
        
        return res;
    }

    @Transactional
    public Boolean deleteDoctor(UUID id) {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> {
            log.warn("Doctor has been deleted");
            return new NotFoundException("Doctor has been deleted");
        });

        if (doctor.getDeletedAt() != null) {
            return false; // idempotent
        }

        doctor.setDeletedAt(LocalDateTime.now());
        doctorRepository.save(doctor);
        
        UserAccount account = doctor.getUserAccount();
        account.setDeletedAt(LocalDateTime.now());
        userAccountRepository.save(account);

        return true;
    }

}
