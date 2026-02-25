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
import com.dibimbing.medicareflow.entity.Doctor;
import com.dibimbing.medicareflow.entity.Patient;
import com.dibimbing.medicareflow.entity.UserAccount;
import com.dibimbing.medicareflow.enums.DoctorStatus;
import com.dibimbing.medicareflow.enums.Role;
import com.dibimbing.medicareflow.exception.ConflictException;
import com.dibimbing.medicareflow.exception.NotFoundException;
import com.dibimbing.medicareflow.helper.JwtHelper;
import com.dibimbing.medicareflow.helper.SecurityHelper;
import com.dibimbing.medicareflow.repository.DoctorRepository;
import com.dibimbing.medicareflow.repository.PatientRepository;
import com.dibimbing.medicareflow.repository.UserAccountRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserAccountRepository userAccountRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AuthenticationManager authenticationManager;
    private final TokenBlacklistService blacklistService;
    private final PasswordEncoder passwordEncoder;
    private final JwtHelper jwtHelper;

    @Transactional
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

        userAccount = userAccountRepository.save(userAccount);

        if(registerRequest.getRole() == Role.DOCTOR) {
            Doctor doctor = new Doctor();
            doctor.setName(registerRequest.getName());
            doctor.setSpecialization(registerRequest.getSpecialization());
            doctor.setStatus(DoctorStatus.ACTIVE);
            doctor.setUserAccount(userAccount);
            doctorRepository.save(doctor);
            
        } else if(registerRequest.getRole() == Role.PATIENT) {
            Patient patient = new Patient();
            patient.setName(registerRequest.getName());
            patient.setPhone(registerRequest.getPhone());
            patient.setUserAccount(userAccount);
            patientRepository.save(patient);
        }

        
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

        String token = jwtHelper.generateToken(userAccount.get());

        return new LoginResponse(userAccount.get().getUsername(), userAccount.get().getRole().toString(), token);
    }

    public String logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if(token != null && token.startsWith("Bearer ")) {
            token = token.substring(7).trim();
            log.debug("Logout request for token: {}", token);
            long reminingMillis = jwtHelper.getRemainingTime(token);

            if(reminingMillis > 0) {
                log.info("Blacklisting token for {} ms", reminingMillis);
                blacklistService.blacklistToken(token, reminingMillis);
            } else {
                log.debug("Token already expired, no need to blacklist");
            }
        } else {
            log.warn("Logout attempt without a valid Bearer token");
        }

        String username = SecurityHelper.getCurrentUsername();
        return "Logout as " + username + " successful";
    }

    public String me() {
        return SecurityHelper.getCurrentUsername();
    }

}
