package com.example.preparcialarle.service;

import com.example.preparcialarle.dto.auth.AuthResponse;
import com.example.preparcialarle.dto.auth.LoginRequest;
import com.example.preparcialarle.dto.auth.RegisterRequest;
import com.example.preparcialarle.dto.auth.UserProfileResponse;
import com.example.preparcialarle.model.Role;
import com.example.preparcialarle.model.User;
import com.example.preparcialarle.repository.UserRepository;
import com.example.preparcialarle.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("El email ya esta registrado");
        }
        if (userRepository.existsByCedula(request.cedula())) {
            throw new IllegalArgumentException("La cedula ya esta registrada");
        }
        User user = new User();
        user.setName(request.name());
        user.setCedula(request.cedula());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.USER);
        userRepository.save(user);
        var userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
        return toAuthResponse(user, jwtService.generateToken(userDetails));
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Credenciales invalidas"));
        var userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
        return toAuthResponse(user, jwtService.generateToken(userDetails));
    }

    public UserProfileResponse me(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        return new UserProfileResponse(user.getName(), user.getCedula(), user.getEmail(), user.getRole().name());
    }

    private AuthResponse toAuthResponse(User user, String token) {
        return new AuthResponse(
                token,
                "Bearer",
                user.getName(),
                user.getCedula(),
                user.getEmail(),
                user.getRole().name()
        );
    }
}
