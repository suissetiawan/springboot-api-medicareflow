package com.dibimbing.medicareflow.exception;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.dibimbing.medicareflow.dto.BaseResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper;


@Component
public class CustomAuthenticationHandler implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        BaseResponse<?> baseResponse = BaseResponse.builder()
                .responseStatus(HttpServletResponse.SC_UNAUTHORIZED)
                .message("Unauthorized")
                .build();

        response.getWriter().write(objectMapper.writeValueAsString(baseResponse));
    }

}
