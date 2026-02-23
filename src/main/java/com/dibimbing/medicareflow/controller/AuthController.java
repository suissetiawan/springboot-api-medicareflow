package com.dibimbing.medicareflow.controller;

import org.springframework.web.bind.annotation.RestController;

import com.dibimbing.medicareflow.dto.request.LoginRequest;
import com.dibimbing.medicareflow.dto.request.RegisterRequest;
import com.dibimbing.medicareflow.helper.ResponseHelper;
import com.dibimbing.medicareflow.service.AuthService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseHelper.custom(authService.register(registerRequest), "Register successful", HttpStatus.CREATED);
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        return ResponseHelper.success(authService.login(loginRequest), "Login successful");
    }

}
