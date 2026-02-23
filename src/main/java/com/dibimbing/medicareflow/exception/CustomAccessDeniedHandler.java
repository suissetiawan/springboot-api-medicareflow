package com.dibimbing.medicareflow.exception;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.dibimbing.medicareflow.dto.BaseResponse;
import tools.jackson.databind.ObjectMapper;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        BaseResponse<?> baseResponse = BaseResponse.builder()
                .responseStatus(HttpServletResponse.SC_FORBIDDEN)
                .message("Access Denied")
                .build();

        response.getWriter().write(objectMapper.writeValueAsString(baseResponse));
    }

}
