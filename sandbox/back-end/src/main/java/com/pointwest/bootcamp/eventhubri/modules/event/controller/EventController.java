package com.pointwest.bootcamp.eventhubri.modules.event.controller;

import com.pointwest.bootcamp.eventhubri.modules.event.dto.EventCreateRequestDto;
import com.pointwest.bootcamp.eventhubri.modules.event.dto.EventResponseDto;
import com.pointwest.bootcamp.eventhubri.modules.event.dto.EventUpdateRequestDto;
import com.pointwest.bootcamp.eventhubri.modules.event.entity.Event;
import com.pointwest.bootcamp.eventhubri.modules.event.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Sort;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

        private final EventService eventService;

        @PostMapping
        @PreAuthorize("hasAuthority('MANAGE_EVENTS')")
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

        // @GetMapping
        // @PreAuthorize("hasAuthority('VIEW_ALL_ORGANIZATIONS')")
        // public ResponseEntity<List<EventResponseDto>> getAllEvents(
        // Authentication authentication) {

        // return ResponseEntity.ok(
        // eventService.getAllEvents(
        // authentication.getName()));
        // }

        @GetMapping("/{eventId}")
        @PreAuthorize("hasAuthority('MANAGE_AGENDA')")
        public ResponseEntity<EventResponseDto> getEventById(
                        @PathVariable Long eventId,
                        Authentication authentication) {

                return ResponseEntity.ok(
                                eventService.getEventById(
                                                eventId,
                                                authentication.getName()));
        }

        // Pagination:

        @GetMapping
        @PreAuthorize("hasAuthority('MANAGE_EVENTS')")
        public ResponseEntity<Page<EventResponseDto>> getEvents(
                        @RequestParam(required = false) Event.Status status,
                        @RequestParam(required = false) Event.EventType eventType,
                        @RequestParam(required = false) String search,
                        @PageableDefault(size = 10, sort = "startTime", direction = Sort.Direction.DESC) Pageable pageable,
                        Authentication authentication) {

                Page<EventResponseDto> response = eventService.getOrganizationEventsPaginated(
                                status,
                                eventType,
                                search,
                                pageable,
                                authentication.getName());

                return ResponseEntity.ok(response);
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