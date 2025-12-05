package com.smartqueue.controller;

import com.smartqueue.model.QueueEntry;
import com.smartqueue.service.QueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminQueueController {

    @Autowired
    private QueueService queueService;

    @GetMapping("/queue/{eventId}")
    public ResponseEntity<?> getQueueByEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(queueService.getQueueByEvent(eventId));
    }

    @PostMapping("/queue/next/{eventId}")
    public ResponseEntity<QueueEntry> callNext(@PathVariable Long eventId) {
        QueueEntry next = queueService.callNext(eventId);
        return ResponseEntity.ok(next);
    }

    @PostMapping("/queue/{ticketId}/complete")
    public ResponseEntity<QueueEntry> completeTicket(@PathVariable Long ticketId) {
        QueueEntry completed = queueService.completeTicket(ticketId);
        return ResponseEntity.ok(completed);
    }

    @PostMapping("/queue/{ticketId}/skip")
    public ResponseEntity<QueueEntry> skipTicket(@PathVariable Long ticketId) {
        QueueEntry skipped = queueService.skipTicket(ticketId);
        return ResponseEntity.ok(skipped);
    }

    @GetMapping("/tickets")
    public ResponseEntity<?> getAllActiveTickets() {
        return ResponseEntity.ok(queueService.getAllActiveTickets());
    }

    @PostMapping("/call-next/{eventId}")
    public ResponseEntity<QueueEntry> callNextCustomer(@PathVariable Long eventId) {
        QueueEntry next = queueService.callNext(eventId);
        return ResponseEntity.ok(next);
    }

    @PostMapping("/mark-done/{ticketId}")
    public ResponseEntity<QueueEntry> markTicketDone(@PathVariable Long ticketId) {
        QueueEntry completed = queueService.completeTicket(ticketId);
        return ResponseEntity.ok(completed);
    }

    @PostMapping("/skip/{ticketId}")
    public ResponseEntity<QueueEntry> skipTicketById(@PathVariable Long ticketId) {
        QueueEntry skipped = queueService.skipTicket(ticketId);
        return ResponseEntity.ok(skipped);
    }
}