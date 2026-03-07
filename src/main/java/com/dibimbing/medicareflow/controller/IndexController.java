package com.dibimbing.medicareflow.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dibimbing.medicareflow.dto.BaseResponse;
import com.dibimbing.medicareflow.helper.ResponseHelper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "0. API INFORMATION", description = "Base landing page and system information")
public class IndexController {

    @GetMapping("/")
    @Operation(summary = "API Landing Page", description = "Provides basic information and documentation links for the MediCareFlow API")
    public ResponseEntity<BaseResponse<Map<String, Object>>> getIndex() {
        Map<String, Object> apiInfo = new LinkedHashMap<>();
        apiInfo.put("project", "MediCareFlow - Medical Consultation Booking API");
        apiInfo.put("version", "0.0.1-SNAPSHOT");
        apiInfo.put("description", "A production-ready medical booking engine built with Spring Boot, JPA, and Redis.");
        
        Map<String, String> links = new LinkedHashMap<>();
        links.put("swagger_ui", "/docs/swagger-ui.html");
        links.put("api_docs_json", "/docs/api-docs");
        links.put("github_repository", "https://github.com/suissetiawan/springboot-api-medicareflow");
        apiInfo.put("documentation_links", links);

        Map<String, String> author = new LinkedHashMap<>();
        author.put("name", "Suis Setiawan");
        author.put("linkedin", "https://www.linkedin.com/in/suis-setiawan-79a984236/");
        author.put("portfolio", "https://suissetiawan.my.id");
        apiInfo.put("author", author);

        return ResponseHelper.successOK(apiInfo, "Welcome to MediCareFlow API");
    }
}
