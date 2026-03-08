package com.dibimbing.medicareflow.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI medicareFlowOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("MediCareFlow API").version("v1.0")
                        .description("Comprehensive Healthcare Management System API Documentation"))
                .addSecurityItem(new SecurityRequirement().addList("BearerAuth"))
                .components(new Components().addSecuritySchemes("BearerAuth", new SecurityScheme()
                        .name("Authorization").type(SecurityScheme.Type.HTTP)
                        .scheme("bearer").bearerFormat("JWT")));
    }

    // --- 1. ALL ENDPOINTS ---
    @Bean
    public GroupedOpenApi allApi() {
        return GroupedOpenApi.builder()
                .group("0. ALL ENDPOINTS")
                .pathsToMatch("/api/**")
                .build();
    }

    // --- 2. FLOW BASED GROUPS (Bussiness Process) ---
    @Bean
    public GroupedOpenApi iamFlow() {
        return GroupedOpenApi.builder()
                .group("flow-1. Identity & Access Management")
                .pathsToMatch("/api/auth/**", "/api/users/**")
                .build();
    }

    @Bean
    public GroupedOpenApi masterDataFlow() {
        return GroupedOpenApi.builder()
                .group("flow-2. Master Data Management")
                .pathsToMatch("/api/doctors/**", "/api/patients/**", "/api/consultationtypes/**")
                .build();
    }

    @Bean
    public GroupedOpenApi schedulingFlow() {
        return GroupedOpenApi.builder()
                .group("flow-3. Scheduling & Appointment")
                .pathsToMatch("/api/work-schedule/**", "/api/slot-time/**", "/api/appointments/**")
                .build();
    }

    @Bean
    public GroupedOpenApi consultationFlow() {
        return GroupedOpenApi.builder()
                .group("flow-4. Consultation & Records")
                .pathsToMatch("/api/appointments/*/records", "/api/appointments/records/**")
                .build();
    }

    // --- 3. USER ACCESS GROUPS (Role Based) ---
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("role-1. PUBLIC (No Auth)")
                .pathsToMatch("/api/auth/register", "/api/auth/login")
                .build();
    }

    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("role-2. ADMIN ACCESS")
                .pathsToMatch(
                        "/api/users/**",
                        "/api/work-schedule/**",
                        "/api/slot-time/**",
                        "/api/consultationtypes/**",
                        "/api/doctors/**",
                        "/api/patients/**",
                        "/api/appointments",
                        "/api/appointments/**"
                )
                .pathsToExclude(
                        "/api/appointments/records/my"
                )
                .build();
    }

    @Bean
    public GroupedOpenApi doctorApi() {
        return GroupedOpenApi.builder()
                .group("role-3. DOCTOR ACCESS")
                .pathsToMatch(
                        "/api/appointments/*/records",
                        "/api/appointments/*/status",
                        "/api/appointments/my",
                        "/api/appointments/records/my",
                        "/api/consultationtypes/doctor/**"
                )
                .build();
    }

    @Bean
    public GroupedOpenApi patientApi() {
        return GroupedOpenApi.builder()
                .group("role-4. PATIENT ACCESS")
                .pathsToMatch(
                        "/api/appointments",
                        "/api/appointments/my",
                        "/api/slot-time",
                        "/api/appointments/records/my",
                        "/api/consultationtypes/doctor/**"
                )
                .addOpenApiCustomizer(openApi -> {
                        if (openApi.getPaths().containsKey("/api/appointments")) {
                                openApi.getPaths().get("/api/appointments")
                                .setGet(null);
                        }
                })
                .build();
    }
}
