package com.dibimbing.medicareflow.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dibimbing.medicareflow.dto.request.RoleUpdateRequest;
import com.dibimbing.medicareflow.dto.request.UserUpdateRequest;
import com.dibimbing.medicareflow.dto.response.UserResponse;
import com.dibimbing.medicareflow.entity.Doctor;
import com.dibimbing.medicareflow.entity.Patient;
import com.dibimbing.medicareflow.entity.UserAccount;
import com.dibimbing.medicareflow.enums.Role;
import com.dibimbing.medicareflow.exception.NotFoundException;
import com.dibimbing.medicareflow.helper.DateHelper;
import com.dibimbing.medicareflow.repository.DoctorRepository;
import com.dibimbing.medicareflow.repository.PatientRepository;
import com.dibimbing.medicareflow.repository.UserAccountRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserAccountRepository userAccountRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public Page<UserResponse> getAll(String type, Pageable pageable) {
        Page<UserAccount> users;

        if (type == null || type.isBlank() || type.equalsIgnoreCase("all")) {
            users = userAccountRepository.findAll(pageable);
        } else {
            Role role = Role.valueOf(type.toUpperCase());
            users = userAccountRepository.findByRole(role, pageable);
        }
        return users.map(this::mapToResponse);
    }

    public UserResponse updateRole(UUID id, RoleUpdateRequest req) {
        UserAccount user = userAccountRepository.findById(id).orElseThrow(() -> {
            log.warn("User not found");
            return new NotFoundException("User not found");
        });

        user.setRole(req.getRole());
        userAccountRepository.save(user);

        return mapToResponse(user);
    }

    public UserResponse getUserById(UUID id) {
        UserAccount user = userAccountRepository.findById(id).orElseThrow(() -> {
            log.warn("User not found");
            return new NotFoundException("User not found");
        });
        return mapToResponse(user);
    }

    @Transactional
    public UserResponse updateUser(UUID id, UserUpdateRequest req) {
        UserAccount user = userAccountRepository.findById(id).orElseThrow(() -> {
            log.warn("User not found");
            return new NotFoundException("User not found");
        });

        if (req.getEmail() != null) {
            user.setEmail(req.getEmail());
        }
        userAccountRepository.save(user);

        if (user.getRole() == Role.PATIENT && user.getPatient() != null) {
            Patient patient = user.getPatient();
            if (req.getName() != null) patient.setName(req.getName());
            if (req.getPhone() != null) patient.setPhone(req.getPhone());
            patientRepository.save(patient);
        } else if (user.getRole() == Role.DOCTOR && user.getDoctor() != null) {
            Doctor doctor = user.getDoctor();
            if (req.getName() != null) doctor.setName(req.getName());
            if (req.getSpecialization() != null) doctor.setSpecialization(req.getSpecialization());
            doctorRepository.save(doctor);
        }

        return mapToResponse(user);
    }

    @Transactional
    public Boolean deleteUser(UUID id) {
        UserAccount user = userAccountRepository.findById(id).orElseThrow(() -> {
            log.warn("User already deleted or not found");
            return new NotFoundException("User not found");
        });

        if (user.getDeletedAt() != null) {
            return false;
        }

        LocalDateTime now = LocalDateTime.now();
        user.setDeletedAt(now);
        userAccountRepository.save(user);

        // Soft delete associated profiles
        if (user.getRole() == Role.PATIENT && user.getPatient() != null) {
            Patient patient = user.getPatient();
            patient.setDeletedAt(now);
            patientRepository.save(patient);
        } else if (user.getRole() == Role.DOCTOR && user.getDoctor() != null) {
            Doctor doctor = user.getDoctor();
            doctor.setDeletedAt(now);
            doctorRepository.save(doctor);
        }

        return true;
    }

    private UserResponse mapToResponse(UserAccount user) {
        UserResponse res = new UserResponse();
        
        res.setId(user.getId().toString());
        res.setUsername(user.getUsername());
        res.setEmail(user.getEmail());
        res.setRole(user.getRole().name());
        res.setCreatedAt(DateHelper.format(user.getCreatedAt()));
        res.setUpdatedAt(DateHelper.format(user.getUpdatedAt()));
        return res;
    }
}
