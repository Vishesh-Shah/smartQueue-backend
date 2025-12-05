package com.smartqueue.service;

import com.smartqueue.dto.AuthResponse;
import com.smartqueue.dto.LoginRequest;
import com.smartqueue.model.AdminUser;
import com.smartqueue.repository.AdminUserRepository;
import com.smartqueue.config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminAuthService {

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthResponse login(LoginRequest request) {
        Optional<AdminUser> userOpt = adminUserRepository.findByUsername(request.getIdentifier());
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Invalid credentials");
        }

        AdminUser user = userOpt.get();
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getUsername(), "ADMIN", user.getId());
        return new AuthResponse(token, user.getId(), user.getUsername());
    }
}
