package com.smartqueue.controller;

import com.smartqueue.dto.TicketRequest;
import com.smartqueue.dto.TicketResponse;
import com.smartqueue.service.QueueService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CustomerTicketController {

    @Autowired
    private QueueService queueService;



    @PostMapping("/tickets")
    public ResponseEntity<TicketResponse> generateTicket(@RequestBody TicketRequest request, HttpServletRequest httpRequest) {
        Long customerId = (Long) httpRequest.getAttribute("userId");
        TicketResponse response = queueService.generateTicket(request, customerId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/tickets/book/{eventId}")
    public ResponseEntity<TicketResponse> bookTicket(@PathVariable Long eventId, HttpServletRequest httpRequest) {
        Long customerId = (Long) httpRequest.getAttribute("userId");
        TicketRequest request = new TicketRequest();
        request.setEventId(eventId);
        request.setCustomerName("Customer");
        request.setPhone("N/A");
        TicketResponse response = queueService.generateTicket(request, customerId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/tickets/{ticketCode}")
    public ResponseEntity<TicketResponse> getTicketStatus(@PathVariable String ticketCode) {
        TicketResponse response = queueService.getTicketStatus(ticketCode);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/tickets/{ticketCode}/notification")
    public ResponseEntity<?> checkNotification(@PathVariable String ticketCode) {
        try {
            TicketResponse ticket = queueService.getTicketStatus(ticketCode);
            boolean isYourTurn = "IN_PROGRESS".equals(ticket.getStatus());
            return ResponseEntity.ok(Map.of(
                "isYourTurn", isYourTurn,
                "status", ticket.getStatus(),
                "position", ticket.getPosition(),
                "message", isYourTurn ? "It's your turn! Please proceed to the counter." : "Please wait for your turn."
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}
