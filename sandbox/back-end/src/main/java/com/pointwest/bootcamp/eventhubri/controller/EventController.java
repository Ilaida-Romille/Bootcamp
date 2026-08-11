package com.pointwest.bootcamp.eventhubri.controller;

import com.pointwest.bootcamp.eventhubri.dto.CreateEventRequest;
import com.pointwest.bootcamp.eventhubri.dto.EventResponse;
import com.pointwest.bootcamp.eventhubri.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    // 1. Create a new Event
    @PostMapping
    public ResponseEntity<EventResponse> createEvent(@RequestBody CreateEventRequest request) {
        EventResponse createdEvent = eventService.createEvent(request);
        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }

    // 2. Get all Events
    @GetMapping
    public ResponseEntity<List<EventResponse>> getAllEvents() {
        List<EventResponse> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    // 3. Get an Event by its eventId (e.g., /api/events/EVT-a1b2c3d4)
    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponse> getEventById(@PathVariable String eventId) {
        EventResponse event = eventService.getEventByEventId(eventId);
        return ResponseEntity.ok(event);
    }

    // 4. Update an Event
    @PutMapping("/{eventId}")
    public ResponseEntity<EventResponse> updateEvent(
            @PathVariable String eventId,
            @RequestBody CreateEventRequest request) {
        EventResponse updatedEvent = eventService.updateEvent(eventId, request);
        return ResponseEntity.ok(updatedEvent);
    }

    // 5. Delete an Event
    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable String eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }
}