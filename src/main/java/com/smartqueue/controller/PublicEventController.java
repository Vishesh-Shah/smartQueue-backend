package com.smartqueue.controller;

import com.smartqueue.model.Event;
import com.smartqueue.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "http://localhost:3000")
public class PublicEventController {

    @Autowired
    private EventRepository eventRepository;

    @GetMapping
    public ResponseEntity<List<Event>> getAllActiveEvents() {
        System.out.println("=== FETCHING EVENTS FOR CUSTOMERS ===");
        List<Event> activeEvents = eventRepository.findByIsActiveTrue();
        System.out.println("Found " + activeEvents.size() + " active events");
        for (Event event : activeEvents) {
            System.out.println("Event: " + event.getEventName() + ", Active: " + event.getIsActive());
        }
        return ResponseEntity.ok(activeEvents);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEvent(@PathVariable Long id) {
        return eventRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}