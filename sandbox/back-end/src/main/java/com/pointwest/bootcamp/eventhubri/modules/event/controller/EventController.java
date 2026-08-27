package com.pointwest.bootcamp.eventhubri.modules.event.controller;

import com.pointwest.bootcamp.eventhubri.modules.event.dto.EventCreateRequestDto;
import com.pointwest.bootcamp.eventhubri.modules.event.dto.EventResponseDto;
import com.pointwest.bootcamp.eventhubri.modules.event.dto.EventUpdateRequestDto;
import com.pointwest.bootcamp.eventhubri.modules.event.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<EventResponseDto> createEvent(
            @Valid @RequestBody EventCreateRequestDto request,
            Authentication authentication) {

        EventResponseDto response = eventService.createEvent(
                request,
                authentication.getName());

        return ResponseEntity
                .created(
                        URI.create("/api/events/" + response.id()))
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<EventResponseDto>> getAllEvents(
            Authentication authentication) {

        return ResponseEntity.ok(
                eventService.getAllEvents(
                        authentication.getName()));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponseDto> getEventById(
            @PathVariable Long eventId,
            Authentication authentication) {

        return ResponseEntity.ok(
                eventService.getEventById(
                        eventId,
                        authentication.getName()));
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<EventResponseDto> updateEvent(
            @PathVariable Long eventId,
            @Valid @RequestBody EventUpdateRequestDto request,
            Authentication authentication) {

        return ResponseEntity.ok(
                eventService.updateEvent(
                        eventId,
                        request,
                        authentication.getName()));
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(
            @PathVariable Long eventId,
            Authentication authentication) {

        eventService.deleteEvent(
                eventId,
                authentication.getName());

        return ResponseEntity.noContent().build();
    }
}