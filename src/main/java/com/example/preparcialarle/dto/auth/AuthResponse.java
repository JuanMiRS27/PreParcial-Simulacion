package com.example.preparcialarle.dto.auth;

public record AuthResponse(
        String token,
        String type,
        String name,
        String cedula,
        String email,
        String role
) {}
