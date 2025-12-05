package com.smartqueue.controller;

import com.smartqueue.dto.AuthResponse;
import com.smartqueue.dto.LoginRequest;
import com.smartqueue.config.JwtUtil;
import com.smartqueue.model.AdminUser;
import com.smartqueue.repository.AdminUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminAuthController {

    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private AdminUserRepository adminUserRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // Hardcoded admin users
            if ("admin1".equals(request.getIdentifier()) && "admin123".equals(request.getPassword())) {
                String token = jwtUtil.generateToken("admin1", "ADMIN", 1L);
                return ResponseEntity.ok(new AuthResponse(token, 1L, "admin1"));
            }
            if ("admin2".equals(request.getIdentifier()) && "admin123".equals(request.getPassword())) {
                String token = jwtUtil.generateToken("admin2", "ADMIN", 2L);
                return ResponseEntity.ok(new AuthResponse(token, 2L, "admin2"));
            }
            // Legacy admin
            if ("admin".equals(request.getIdentifier()) && "admin123".equals(request.getPassword())) {
                String token = jwtUtil.generateToken("admin", "ADMIN", 1L);
                return ResponseEntity.ok(new AuthResponse(token, 1L, "admin"));
            }
            
            // Try database admin users if not hardcoded
            Optional<AdminUser> adminOpt = adminUserRepository.findByUsername(request.getIdentifier());
            if (adminOpt.isPresent()) {
                AdminUser admin = adminOpt.get();
                if (passwordEncoder.matches(request.getPassword(), admin.getPasswordHash())) {
                    String token = jwtUtil.generateToken(admin.getUsername(), "ADMIN", admin.getId());
                    return ResponseEntity.ok(new AuthResponse(token, admin.getId(), admin.getUsername()));
                }
            }
            
            return ResponseEntity.badRequest().body(java.util.Map.of("message", "Invalid credentials"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("message", "Login failed: " + e.getMessage()));
        }
    }
}
