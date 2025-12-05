package com.smartqueue.service;

import com.smartqueue.dto.AuthResponse;
import com.smartqueue.dto.LoginRequest;
import com.smartqueue.dto.SignUpRequest;
import com.smartqueue.model.CustomerUser;
import com.smartqueue.repository.CustomerUserRepository;
import com.smartqueue.config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private CustomerUserRepository customerUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthResponse signUp(SignUpRequest request) {
        return signup(request);
    }

    public AuthResponse signup(SignUpRequest request) {
        if (customerUserRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        CustomerUser user = new CustomerUser();
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setPhone(request.getPhone());
        user.setCreatedAt(java.time.ZonedDateTime.now());

        customerUserRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail(), "CUSTOMER", user.getId());
        return new AuthResponse(token, user.getId(), user.getEmail());
    }

    public AuthResponse login(LoginRequest request) {
        try {
            // Customer test users - pattern: user1@gmail.com to user5@gmail.com, all password: "password"
            if ("user1@gmail.com".equals(request.getIdentifier()) && "password".equals(request.getPassword())) {
                String token = jwtUtil.generateToken("user1@gmail.com", "CUSTOMER", 1L);
                return new AuthResponse(token, 1L, "user1@gmail.com");
            }
            if ("user2@gmail.com".equals(request.getIdentifier()) && "password".equals(request.getPassword())) {
                String token = jwtUtil.generateToken("user2@gmail.com", "CUSTOMER", 2L);
                return new AuthResponse(token, 2L, "user2@gmail.com");
            }
            if ("user3@gmail.com".equals(request.getIdentifier()) && "password".equals(request.getPassword())) {
                String token = jwtUtil.generateToken("user3@gmail.com", "CUSTOMER", 3L);
                return new AuthResponse(token, 3L, "user3@gmail.com");
            }
            if ("user4@gmail.com".equals(request.getIdentifier()) && "password".equals(request.getPassword())) {
                String token = jwtUtil.generateToken("user4@gmail.com", "CUSTOMER", 4L);
                return new AuthResponse(token, 4L, "user4@gmail.com");
            }
            if ("user5@gmail.com".equals(request.getIdentifier()) && "password".equals(request.getPassword())) {
                String token = jwtUtil.generateToken("user5@gmail.com", "CUSTOMER", 5L);
                return new AuthResponse(token, 5L, "user5@gmail.com");
            }
            
            // Admin test users
            if ("admin1".equals(request.getIdentifier()) && "admin123".equals(request.getPassword())) {
                String token = jwtUtil.generateToken("admin1", "ADMIN", 1L);
                return new AuthResponse(token, 1L, "admin1");
            }
            if ("admin2".equals(request.getIdentifier()) && "admin123".equals(request.getPassword())) {
                String token = jwtUtil.generateToken("admin2", "ADMIN", 2L);
                return new AuthResponse(token, 2L, "admin2");
            }
            
            // Try database users if not hardcoded
            Optional<CustomerUser> userOpt = customerUserRepository.findByEmail(request.getIdentifier());
            if (userOpt.isPresent()) {
                CustomerUser user = userOpt.get();
                if (passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
                    String token = jwtUtil.generateToken(user.getEmail(), "CUSTOMER", user.getId());
                    return new AuthResponse(token, user.getId(), user.getEmail());
                }
            }
            
            throw new RuntimeException("Invalid credentials");
        } catch (Exception e) {
            throw new RuntimeException("Invalid credentials");
        }
    }
}