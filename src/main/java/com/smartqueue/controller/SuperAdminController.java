package com.smartqueue.controller;

import com.smartqueue.dto.AuthResponse;
import com.smartqueue.dto.LoginRequest;
import com.smartqueue.model.AdminRequest;
import com.smartqueue.model.AdminUser;
import com.smartqueue.repository.AdminRequestRepository;
import com.smartqueue.repository.AdminUserRepository;
import com.smartqueue.config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/super-admin")
@CrossOrigin(origins = "http://localhost:3000")
public class SuperAdminController {

    @Autowired
    private AdminRequestRepository adminRequestRepository;

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // Super admin credentials 
    private static final String SUPER_ADMIN_USERNAME = "VisheshShah";
    private static final String SUPER_ADMIN_PASSWORD = "vishesh@2003";

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // Super admin credentials
            if (!SUPER_ADMIN_USERNAME.equals(request.getIdentifier()) || !SUPER_ADMIN_PASSWORD.equals(request.getPassword())) {
                return ResponseEntity.badRequest().body(java.util.Map.of("message", "Invalid super admin credentials"));
            }
            
            String token = jwtUtil.generateToken("superadmin", "SUPER_ADMIN", 0L);
            return ResponseEntity.ok(new AuthResponse(token, 0L, "superadmin"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("message", "Login failed: " + e.getMessage()));
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Super Admin API is working");
    }

    @GetMapping("/requests")
    public ResponseEntity<List<AdminRequest>> getAllRequests() {
        System.out.println("=== SUPER ADMIN FETCHING REQUESTS ===");
        try {
            List<AdminRequest> requests = adminRequestRepository.findAllByOrderByCreatedAtDesc();
            System.out.println("Found " + requests.size() + " admin requests");
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            System.out.println("Error fetching requests: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @PostMapping("/requests/{id}/approve")
    public ResponseEntity<String> approveRequest(@PathVariable Long id) {
        Optional<AdminRequest> requestOpt = adminRequestRepository.findById(id);
        if (requestOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        AdminRequest request = requestOpt.get();
        
        // Just approve the request - don't create admin account yet
        request.setStatus("APPROVED");
        adminRequestRepository.save(request);
        
        String response = String.format("Request approved! Business %s can now create their admin account using email: %s", request.getBusinessName(), request.getEmail());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/requests/{id}/reject")
    public ResponseEntity<String> rejectRequest(@PathVariable Long id) {
        Optional<AdminRequest> requestOpt = adminRequestRepository.findById(id);
        if (requestOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        AdminRequest request = requestOpt.get();
        request.setStatus("REJECTED");
        adminRequestRepository.save(request);
        
        return ResponseEntity.ok("Request rejected");
    }
}