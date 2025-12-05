package com.smartqueue.controller;

import com.smartqueue.model.Event;
import com.smartqueue.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminEventController {

    @Autowired
    private EventRepository eventRepository;

    @GetMapping("/events")
    public ResponseEntity<?> getAdminEvents() {
        return ResponseEntity.ok(eventRepository.findByIsActiveTrue());
    }

    @PostMapping("/events")
    public ResponseEntity<?> createEvent(@RequestBody EventRequest request) {
        try {
            Event event = new Event();
            event.setEventName(request.getEventName());
            event.setMaxTokens(request.getMaxTokens());
            event.setIsActive(true);
            event.setCurrentTokenCount(0);
            event.setAverageServiceMinutes(10);
            event.setAdminId(1L);
            event.setDurationHours(2); // Default duration
            event.setLocation("Default Location"); // Default location
            
            Event savedEvent = eventRepository.save(event);
            System.out.println("=== EVENT SAVED SUCCESSFULLY ===");
            System.out.println("Event ID: " + savedEvent.getId());
            System.out.println("Event Name: " + savedEvent.getEventName());
            System.out.println("Is Active: " + savedEvent.getIsActive());
            return ResponseEntity.ok(savedEvent);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    public static class EventRequest {
        private String eventName;
        private Integer maxTokens;
        
        public String getEventName() { return eventName; }
        public void setEventName(String eventName) { this.eventName = eventName; }
        public Integer getMaxTokens() { return maxTokens; }
        public void setMaxTokens(Integer maxTokens) { this.maxTokens = maxTokens; }
    }
}
