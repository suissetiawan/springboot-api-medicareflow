package com.dibimbing.medicareflow.dto.request;

import com.dibimbing.medicareflow.enums.Role;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RoleUpdateRequest {
    @NotNull(message = "Role is required")
    private Role role;
}
