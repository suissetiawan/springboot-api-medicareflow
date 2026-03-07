package com.dibimbing.medicareflow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth

                // AUTH & PUBLIC
                .requestMatchers("/", "/api/auth/**", "/docs/**").permitAll()

                // CONSULTATION TYPES
                .requestMatchers(HttpMethod.GET, "/api/consultationtypes/doctor/**").hasAnyRole("PATIENT", "DOCTOR", "ADMIN")
                .requestMatchers("/api/consultationtypes/**").hasRole("ADMIN")

                // APPOINTMENTS - RECORDS
                .requestMatchers("/api/appointments/records/my").hasAnyRole("PATIENT", "DOCTOR")
                .requestMatchers(HttpMethod.GET, "/api/appointments/*/records").hasAnyRole("ADMIN", "DOCTOR")
                .requestMatchers(HttpMethod.POST, "/api/appointments/*/records").hasAnyRole("ADMIN", "DOCTOR")

                // APPOINTMENTS - CORE
                .requestMatchers("/api/appointments/my").hasAnyRole("PATIENT", "DOCTOR")
                .requestMatchers(HttpMethod.POST, "/api/appointments").hasRole("PATIENT")
                .requestMatchers("/api/appointments/*/status").hasAnyRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/appointments").hasRole("ADMIN")

                // ADMIN MODULES (USER, DOCTOR, PATIENT, SCHEDULE, SLOT)
                .requestMatchers("/api/users/**").hasRole("ADMIN")
                .requestMatchers("/api/doctors/**").hasRole("ADMIN")
                .requestMatchers("/api/patients/**").hasRole("ADMIN")
                .requestMatchers("/api/work-schedule/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/slot-time/**").hasAnyRole("PATIENT", "ADMIN")
                .requestMatchers("/api/slot-time/**").hasRole("ADMIN")


                .anyRequest().authenticated())
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(customAuthenticationHandler)
                .accessDeniedHandler(customAccessDeniedHandler))
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
