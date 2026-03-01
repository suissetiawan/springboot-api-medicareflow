package com.dibimbing.medicareflow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.dibimbing.medicareflow.exception.CustomAccessDeniedHandler;
import com.dibimbing.medicareflow.exception.CustomAuthenticationHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final CustomAuthenticationHandler customAuthenticationHandler;
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth

                // PUBLIC
                .requestMatchers("/api/auth/register","/api/auth/login","/docs/**").permitAll()

                // ADMIN
                .requestMatchers("/api/work-schedule/**").hasRole("ADMIN")
                .requestMatchers("/api/slot-time/*").hasRole("ADMIN")
                .requestMatchers("/api/users/**").hasRole("ADMIN")
                .requestMatchers("/api/consultationtypes/**").hasRole("ADMIN")
                .requestMatchers("/api/doctors/*").hasRole("ADMIN")
                .requestMatchers("/api/patients/*").hasRole("ADMIN")
                .requestMatchers("/api/appointments").hasRole("ADMIN")

                // DOCTOR
                .requestMatchers("/api/appointments/*/records").hasRole("DOCTOR")
                .requestMatchers("/api/appointments/*/status").hasRole("DOCTOR")

                // PATIENT
                .requestMatchers("/api/appointments/my").hasAnyRole("PATIENT","DOCTOR")
                .requestMatchers("/api/appointments").hasRole("PATIENT")


                .anyRequest().authenticated())
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(customAuthenticationHandler)
                .accessDeniedHandler(new CustomAccessDeniedHandler()))
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
