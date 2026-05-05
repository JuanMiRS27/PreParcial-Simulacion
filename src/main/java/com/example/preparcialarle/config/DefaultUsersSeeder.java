package com.example.preparcialarle.config;

import com.example.preparcialarle.model.Role;
import com.example.preparcialarle.model.User;
import com.example.preparcialarle.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Profile("auth-admin")
public class DefaultUsersSeeder {

    @Bean
    CommandLineRunner seedDefaultUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            createOrUpdateDefaultUser(userRepository, passwordEncoder, "Usuario Pruebas", "1001001001", "user.test@insurtech.local", "User12345!", Role.USER);
            createOrUpdateDefaultUser(userRepository, passwordEncoder, "Admin Pruebas", "9009009001", "admin.test@insurtech.local", "Admin12345!", Role.ADMIN);
        };
    }

    private void createOrUpdateDefaultUser(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            String name,
            String cedula,
            String email,
            String rawPassword,
            Role role
    ) {
        var existing = userRepository.findByEmail(email);
        if (existing.isPresent()) {
            User user = existing.get();
            if (user.getCedula() == null || user.getCedula().isBlank()) {
                user.setCedula(cedula);
                userRepository.save(user);
            }
            return;
        }
        User user = new User();
        user.setName(name);
        user.setCedula(cedula);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(role);
        userRepository.save(user);
    }
}
