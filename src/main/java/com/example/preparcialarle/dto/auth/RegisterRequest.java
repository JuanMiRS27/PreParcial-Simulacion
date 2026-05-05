package com.example.preparcialarle.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank String name,
        @NotBlank @Size(min = 6, max = 30) String cedula,
        @Email @NotBlank String email,
        @NotBlank @Size(min = 6) String password
) {}
