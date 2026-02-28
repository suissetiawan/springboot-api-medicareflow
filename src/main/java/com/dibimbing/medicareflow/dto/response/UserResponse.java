package com.dibimbing.medicareflow.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
@JsonPropertyOrder({ "id", "username", "role" })
public class UserResponse {
    private String id;
    private String username;
    private String role;
    private String createdAt;
    private String updatedAt;
}
