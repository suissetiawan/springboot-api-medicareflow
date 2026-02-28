package com.dibimbing.medicareflow.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dibimbing.medicareflow.dto.response.UserResponse;
import com.dibimbing.medicareflow.entity.UserAccount;
import com.dibimbing.medicareflow.enums.Role;
import com.dibimbing.medicareflow.repository.DoctorRepository;
import com.dibimbing.medicareflow.repository.PatientRepository;
import com.dibimbing.medicareflow.repository.UserAccountRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserAccountRepository userAccountRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public List<UserResponse> getAll(String type) {
        List<UserAccount> users;

        if (type == null || type.isBlank() || type.equalsIgnoreCase("all")) {
            users = userAccountRepository.findAll();
        } else {
            try {
                Role role = Role.valueOf(type.toUpperCase());
                users = userAccountRepository.findByRole(role);
            } catch (IllegalArgumentException e) {
                log.warn("Invalid role type requested: {}", type);
                users = new ArrayList<>();
            }
        }

        return users.stream().map(this::mapToResponse).toList();
    }

    private UserResponse mapToResponse(UserAccount user) {
        UserResponse res = new UserResponse();
        
        String displayId = user.getId().toString();
        if (user.getRole() == Role.PATIENT) {
            displayId = patientRepository.findByUserAccountId(user.getId())
                    .map(p -> p.getId().toString()).orElse(null);
        } else if (user.getRole() == Role.DOCTOR) {
            displayId = doctorRepository.findByUserAccountId(user.getId())
                    .map(d -> d.getId().toString()).orElse(null);
        }

        res.setId(displayId);
        res.setUsername(user.getUsername());
        res.setRole(user.getRole().name());
        res.setCreatedAt(user.getCreatedAt().toString());
        res.setUpdatedAt(user.getUpdatedAt().toString());
        return res;
    }
}
