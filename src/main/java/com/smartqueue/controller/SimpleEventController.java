package com.smartqueue.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/simple")
@CrossOrigin(origins = "http://localhost:3000")
public class SimpleEventController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/create-event")
    public ResponseEntity<?> createSimpleEvent(@RequestParam String eventName, @RequestParam Integer maxTokens) {
        try {
            String sql = "INSERT INTO event (event_name, max_tokens, current_token_count, average_service_minutes, is_active, admin_id) VALUES (?, ?, 0, 10, true, 1)";
            jdbcTemplate.update(sql, eventName, maxTokens);
            return ResponseEntity.ok("Event created successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}