package com.huyntd.superapp.gundam_shop.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huyntd.superapp.gundam_shop.dto.ApiResponse;
import com.huyntd.superapp.gundam_shop.exception.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;

        response.setStatus(errorCode.getStatusCode().value());
        // MediaType.APPLICATION_JSON_VALUE = "application/json"
        response.setContentType("application/json");

        ObjectMapper objectMapper = new ObjectMapper();

        response.getWriter().write(objectMapper.writeValueAsString(ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .success(false)
                .build()));

        response.flushBuffer();
    }
}
