package com.smartqueue.service;

import com.smartqueue.dto.TicketRequest;
import com.smartqueue.dto.TicketResponse;
import com.smartqueue.model.Event;
import com.smartqueue.model.QueueEntry;
import com.smartqueue.model.CustomerUser;
import com.smartqueue.repository.EventRepository;
import com.smartqueue.repository.QueueEntryRepository;
import com.smartqueue.repository.CustomerUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class QueueService {

    @Autowired
    private QueueEntryRepository queueEntryRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private CustomerUserRepository customerUserRepository;

    public TicketResponse generateTicket(TicketRequest request, Long customerId) {
        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (!event.getIsActive()) {
            throw new RuntimeException("Event is not active");
        }

        Long activeCount = queueEntryRepository.countActiveEntriesByEvent(request.getEventId());
        int position = activeCount.intValue() + 1;

        String ticketCode = generateTicketCode(event.getEventName(), position);

        QueueEntry entry = new QueueEntry();
        entry.setTicketCode(ticketCode);
        entry.setEvent(event);
        entry.setStatus("WAITING");
        entry.setPosition(position);
        entry.setCreatedAt(ZonedDateTime.now());

        if (customerId != null) {
            Optional<CustomerUser> customer = customerUserRepository.findById(customerId);
            if (customer.isPresent()) {
                entry.setCustomer(customer.get());
            } else {
                entry.setCustomerName(request.getCustomerName());
                entry.setPhone(request.getPhone());
            }
        } else {
            entry.setCustomerName(request.getCustomerName());
            entry.setPhone(request.getPhone());
        }

        queueEntryRepository.save(entry);

        // Update event's current token count
        event.setCurrentTokenCount(event.getCurrentTokenCount() + 1);
        eventRepository.save(event);

        return createTicketResponse(entry);
    }

    public TicketResponse getTicketStatus(String ticketCode) {
        QueueEntry entry = queueEntryRepository.findAll().stream()
                .filter(q -> q.getTicketCode().equals(ticketCode))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        return createTicketResponse(entry);
    }

    public List<QueueEntry> getQueueByEvent(Long eventId) {
        return queueEntryRepository.findAllByEventId(eventId);
    }

    @Transactional
    public QueueEntry callNext(Long eventId) {
        List<QueueEntry> waiting = queueEntryRepository.findActiveQueueByEventId(eventId);

        if (waiting.isEmpty()) {
            throw new RuntimeException("No waiting customers");
        }

        QueueEntry next = waiting.get(0);
        next.setStatus("IN_PROGRESS");
        next.setUpdatedAt(ZonedDateTime.now());

        // Shift positions
        for (int i = 1; i < waiting.size(); i++) {
            waiting.get(i).setPosition(waiting.get(i).getPosition() - 1);
        }

        queueEntryRepository.saveAll(waiting);
        queueEntryRepository.save(next);

        // Update event's current token count
        Event event = next.getEvent();
        if (event.getCurrentTokenCount() > 0) {
            event.setCurrentTokenCount(event.getCurrentTokenCount() - 1);
            eventRepository.save(event);
        }

        return next;
    }

    public QueueEntry completeTicket(Long ticketId) {
        QueueEntry entry = queueEntryRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        entry.setStatus("DONE");
        entry.setUpdatedAt(ZonedDateTime.now());
        queueEntryRepository.save(entry);

        return entry;
    }

    public QueueEntry skipTicket(Long ticketId) {
        QueueEntry entry = queueEntryRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        entry.setStatus("SKIPPED");
        entry.setUpdatedAt(ZonedDateTime.now());
        queueEntryRepository.save(entry);

        return entry;
    }

    public List<QueueEntry> getCurrentServing(Long eventId) {
        return queueEntryRepository.findCurrentServingByEventId(eventId);
    }

    public List<QueueEntry> getAllActiveTickets() {
        return queueEntryRepository.findByStatusIn(List.of("WAITING", "IN_PROGRESS"));
    }

    private String generateTicketCode(String eventName, int position) {
        String prefix = eventName.split(" ")[0].toUpperCase();
        return prefix + position;
    }

    private TicketResponse createTicketResponse(QueueEntry entry) {
        Integer estimatedWait = calculateEstimatedWait(entry);
        return new TicketResponse(
                entry.getTicketCode(),
                entry.getEvent().getEventName(),
                entry.getPosition(),
                entry.getStatus(),
                estimatedWait,
                entry.getCreatedAt(),
                entry.getUpdatedAt()
        );
    }

    private Integer calculateEstimatedWait(QueueEntry entry) {
        if ("DONE".equals(entry.getStatus()) || "SKIPPED".equals(entry.getStatus())) {
            return 0;
        }
        return entry.getPosition() * entry.getEvent().getAverageServiceMinutes();
    }
}
