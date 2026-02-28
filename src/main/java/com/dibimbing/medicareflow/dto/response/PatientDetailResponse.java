package com.dibimbing.medicareflow.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
@JsonPropertyOrder({ "id", "name", "username", "email", "phone", "role" })
public class PatientDetailResponse {
    private String id;
    private String name;
    private String username;
    private String email;
    private String phone;
    private String role;
    private String updatedAt;
    private String createdAt;
}
