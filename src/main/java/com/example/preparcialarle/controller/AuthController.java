package com.example.preparcialarle.controller;

import com.example.preparcialarle.dto.auth.AuthResponse;
import com.example.preparcialarle.dto.auth.LoginRequest;
import com.example.preparcialarle.dto.auth.RegisterRequest;
import com.example.preparcialarle.dto.auth.UserProfileResponse;
import com.example.preparcialarle.service.AuthService;
import jakarta.validation.Valid;
import java.security.Principal;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Profile("auth-admin")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/me")
    public UserProfileResponse me(Principal principal) {
        return authService.me(principal.getName());
    }
}
