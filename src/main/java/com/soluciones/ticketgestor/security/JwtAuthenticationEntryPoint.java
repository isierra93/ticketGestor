package com.soluciones.ticketgestor.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.soluciones.ticketgestor.dtos.ErrorDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) 
            throws IOException {
        
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ErrorDto errorDto = new ErrorDto(
                "No autorizado. Se requiere un token JWT válido para acceder a este recurso.",
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                HttpStatus.UNAUTHORIZED.value(),
                LocalDateTime.now()
        );

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // Necesario para serializar LocalDateTime
        
        String jsonResponse = mapper.writeValueAsString(errorDto);
        response.getWriter().write(jsonResponse);
    }
}
