package com.smartqueue.controller;

import com.smartqueue.dto.SignUpRequest;
import com.smartqueue.model.AdminRequest;
import com.smartqueue.model.AdminUser;
import com.smartqueue.repository.AdminRequestRepository;
import com.smartqueue.repository.AdminUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminSignupController {

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private AdminRequestRepository adminRequestRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpRequest request) {
        try {
            // Check if email is approved
            Optional<AdminRequest> approvedRequest = adminRequestRepository.findByEmailAndStatus(request.getEmail(), "APPROVED");
            if (approvedRequest.isEmpty()) {
                return ResponseEntity.badRequest().body(java.util.Map.of("message", "You are not an approved admin user. Please contact the system administrator."));
            }

            // Use name as username if username is not provided
            String username = request.getUsername() != null ? request.getUsername() : request.getName();
            
            // Check if username already exists
            if (adminUserRepository.findByUsername(username).isPresent()) {
                return ResponseEntity.badRequest().body(java.util.Map.of("message", "Username already exists"));
            }

            // Create admin user
            AdminUser adminUser = new AdminUser();
            adminUser.setUsername(username);
            adminUser.setPasswordHash(passwordEncoder.encode(request.getPassword()));
            adminUser.setEmail(request.getEmail());

            adminUserRepository.save(adminUser);
            return ResponseEntity.ok(java.util.Map.of("message", "Admin account created successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("message", "Signup failed: " + e.getMessage()));
        }
    }
}