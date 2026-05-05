package com.example.preparcialarle.dto.auth;

public record UserProfileResponse(
        String name,
        String cedula,
        String email,
        String role
) {}
