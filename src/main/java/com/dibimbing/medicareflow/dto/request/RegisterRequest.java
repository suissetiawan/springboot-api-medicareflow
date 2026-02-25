package com.dibimbing.medicareflow.dto.request;

import com.dibimbing.medicareflow.enums.DoctorStatus;
import com.dibimbing.medicareflow.enums.Role;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String name;
    private String email;
    private String password;
    private Role role;

    // Doctor
    private String specialization;

    // Patient
    private String phone;
}
