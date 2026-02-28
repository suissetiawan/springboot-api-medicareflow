package com.dibimbing.medicareflow.config;

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
                .info(new Info().title("MediCareFlow API").version("v1.0"))
                .addSecurityItem(new SecurityRequirement().addList("BearerAuth"))
                .components(new Components().addSecuritySchemes("BearerAuth", new SecurityScheme()
                                                .name("Authorization").type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer").bearerFormat("JWT")));
    }
}
