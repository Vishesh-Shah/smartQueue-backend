package com.smartqueue.controller;

import com.smartqueue.model.AdminRequest;
import com.smartqueue.repository.AdminRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminRequestController {

    @Autowired
    private AdminRequestRepository adminRequestRepository;

    @GetMapping("/admin-request/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Admin request controller is working");
    }

    @PostMapping("/admin-request")
    public ResponseEntity<?> submitAdminRequest(@RequestBody java.util.Map<String, String> requestData) {
        try {
            System.out.println("Received admin request: " + requestData);
            
            AdminRequest adminRequest = new AdminRequest();
            adminRequest.setBusinessName(requestData.get("businessName"));
            adminRequest.setOwnerName(requestData.get("ownerName"));
            adminRequest.setEmail(requestData.get("email"));
            adminRequest.setPhone(requestData.get("phone"));
            adminRequest.setBusinessAddress(requestData.get("businessAddress"));
            adminRequest.setBusinessType(requestData.get("businessType"));
            
            System.out.println("About to save: " + adminRequest);
            adminRequestRepository.save(adminRequest);
            System.out.println("Saved successfully");
            
            return ResponseEntity.ok("Request submitted successfully");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}