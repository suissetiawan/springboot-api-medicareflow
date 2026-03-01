package com.dibimbing.medicareflow.dto.request;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UserUpdateRequest {
    private String name;
    
    @Email(message = "Email should be valid")
    private String email;
    
    private String phone;
    private String specialization;
}
