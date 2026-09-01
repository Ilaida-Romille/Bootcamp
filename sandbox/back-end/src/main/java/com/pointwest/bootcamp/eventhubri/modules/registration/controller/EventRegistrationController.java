package com.pointwest.bootcamp.eventhubri.modules.registration.controller;

import com.pointwest.bootcamp.eventhubri.modules.registration.dto.RegistrationResponseDto;
import com.pointwest.bootcamp.eventhubri.modules.registration.entity.RegistrationStatus;
import com.pointwest.bootcamp.eventhubri.modules.registration.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events/{eventId}/registrations")
@RequiredArgsConstructor
public class EventRegistrationController {

    private final RegistrationService registrationService;

    @GetMapping
    @PreAuthorize("hasAuthority('CHECK_IN_ATTENDEES') or hasAuthority('MANAGE_EVENTS')")
    public ResponseEntity<Page<RegistrationResponseDto>> listForEvent(
            @PathVariable Long eventId,
            @RequestParam(required = false) RegistrationStatus status,
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable,
            Authentication authentication) {
        return ResponseEntity.ok(
                registrationService.listRegistrationsForEvent(eventId, status, authentication.getName(), pageable));
    }

    @GetMapping("/attendees")
    @PreAuthorize("hasAuthority('REGISTER_FOR_EVENT')")
    public ResponseEntity<Page<RegistrationResponseDto>> listAttendeesPublic(
            @PathVariable Long eventId,
            @PageableDefault(size = 50, sort = "createdAt") Pageable pageable) {
        return ResponseEntity.ok(registrationService.listConfirmedAttendeesForEvent(eventId, pageable));
    }
}
