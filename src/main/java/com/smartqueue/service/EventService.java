package com.smartqueue.service;

import com.smartqueue.model.Event;
import com.smartqueue.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public List<Event> getActiveEvents() {
        return eventRepository.findByIsActiveTrue();
    }
}