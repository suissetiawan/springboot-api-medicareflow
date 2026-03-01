package com.dibimbing.medicareflow.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
@JsonPropertyOrder({ "id", "username", "email", "name", "phone", "specialization", "role" })
public class UserResponse {
    private String id;
    private String username;
    private String email;
    private String name;
    private String phone;
    private String specialization;
    private String role;
    private String createdAt;
    private String updatedAt;
}
