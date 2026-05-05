package com.example.preparcialarle.dto.admin;

public record AdminUserResponse(
        Long id,
        String name,
        String cedula,
        String email,
        String role
) {}
