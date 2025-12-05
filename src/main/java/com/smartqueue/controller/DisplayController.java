package com.smartqueue.controller;

import com.smartqueue.service.QueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/display")
@CrossOrigin(origins = "http://localhost:3000")
public class DisplayController {

    @Autowired
    private QueueService queueService;

    @GetMapping("/current-serving/{eventId}")
    public ResponseEntity<?> getCurrentServing(@PathVariable Long eventId) {
        return ResponseEntity.ok(queueService.getCurrentServing(eventId));
    }

    @GetMapping("/queue/{eventId}")
    public ResponseEntity<?> getQueueStatus(@PathVariable Long eventId) {
        return ResponseEntity.ok(queueService.getQueueByEvent(eventId));
    }
}