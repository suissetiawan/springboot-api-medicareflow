package com.dibimbing.medicareflow.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
@JsonPropertyOrder({ "id", "name", "username", "email", "specialization", "role" })
public class DoctorDetailResponse {
    private String id;
    private String name;
    private String username;
    private String email;
    private String specialization;
    private String role;
    private String createdAt;
    private String updatedAt;
}
