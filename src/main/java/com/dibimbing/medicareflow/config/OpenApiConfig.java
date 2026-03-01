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

    // --- ROLE-BASED GROUPS ---

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("role-public")
                .pathsToMatch("/api/auth/**")
                .build();
    }

    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("role-admin")
                .pathsToMatch(
                        "/api/users/**",
                        "/api/work-schedule/**",
                        "/api/slot-time/**",
                        "/api/consultationtypes/**",
                        "/api/doctors/**",
                        "/api/patients/**",
                        "/api/appointments"
                )
                .build();
    }

    @Bean
    public GroupedOpenApi doctorApi() {
        return GroupedOpenApi.builder()
                .group("role-doctor")
                .pathsToMatch(
                        "/api/appointments/*/records",
                        "/api/appointments/*/status",
                        "/api/appointments/my"
                )
                .build();
    }

    @Bean
    public GroupedOpenApi patientApi() {
        return GroupedOpenApi.builder()
                .group("role-patient")
                .pathsToMatch(
                        "/api/appointments",
                        "/api/appointments/my"
                )
                .build();
    }

    // --- BUSINESS FLOW GROUPS ---

    @Bean
    public GroupedOpenApi iamFlow() {
        return GroupedOpenApi.builder()
                .group("flow-user-management")
                .pathsToMatch("/api/auth/**", "/api/users/**")
                .build();
    }

    @Bean
    public GroupedOpenApi masterDataFlow() {
        return GroupedOpenApi.builder()
                .group("master-data")
                .pathsToMatch("/api/doctors/**", "/api/patients/**", "/api/consultationtypes/**", "/api/users")
                .build();
    }

    @Bean
    public GroupedOpenApi schedulingFlow() {
        return GroupedOpenApi.builder()
                .group("flow-scheduling-appointment")
                .pathsToMatch("/api/work-schedule/**", "/api/slot-time/**", "/api/appointments/**")
                .build();
    }

    @Bean
    public GroupedOpenApi consultationFlow() {
        return GroupedOpenApi.builder()
                .group("flow-consultation")
                .pathsToMatch("/api/appointments/*/records")
                .build();
    }
}
