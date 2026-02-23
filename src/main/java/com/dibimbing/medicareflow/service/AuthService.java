package com.dibimbing.medicareflow.service;

import java.util.Optional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dibimbing.medicareflow.dto.request.LoginRequest;
import com.dibimbing.medicareflow.dto.request.RegisterRequest;
import com.dibimbing.medicareflow.dto.response.LoginResponse;
import com.dibimbing.medicareflow.dto.response.RegisterResponse;
import com.dibimbing.medicareflow.entity.UserAccount;
import com.dibimbing.medicareflow.exception.ConflictException;
import com.dibimbing.medicareflow.exception.NotFoundException;
import com.dibimbing.medicareflow.helper.JwtHelper;
import com.dibimbing.medicareflow.repository.UserAccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserAccountRepository userAccountRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtHelper jwtHelper;

    public RegisterResponse register(RegisterRequest registerRequest) {

        if(userAccountRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new ConflictException("Email already exists");
        }

        if(userAccountRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new ConflictException("Username already exists");
        }

        UserAccount userAccount = new UserAccount();
        userAccount.setUsername(registerRequest.getUsername());
        userAccount.setEmail(registerRequest.getEmail());
        userAccount.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        userAccount.setRole(registerRequest.getRole());

        userAccountRepository.save(userAccount);
        
        return new RegisterResponse(userAccount.getUsername(), userAccount.getEmail(), userAccount.getRole().toString());
    }

    public LoginResponse login(LoginRequest req) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));

        Optional<UserAccount> userAccount;
        if(req.getUsername().contains("@")) {
            userAccount = userAccountRepository.findByEmail(req.getUsername());
        } else {
            userAccount = userAccountRepository.findByUsername(req.getUsername());
        }

        if(userAccount.isEmpty()) {
            throw new NotFoundException("User not found");
        }

        String token = jwtHelper.generateToken(userAccount.get().getUsername());

        return new LoginResponse(userAccount.get().getUsername(), userAccount.get().getRole().toString(), token);
    }

}
