package com.dibimbing.medicareflow.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateRequest {
    @Size(min = 3, message = "Name must be at least 3 characters")
    private String name;

    @Size(min = 3, message = "Username must be at least 3 characters")
    private String username;
    
    @Email(message = "Email should be valid")
    private String email;
    
    private String phone;
    private String specialization;
}
