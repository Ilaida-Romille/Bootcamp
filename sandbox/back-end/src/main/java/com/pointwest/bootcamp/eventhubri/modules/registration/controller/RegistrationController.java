package com.pointwest.bootcamp.eventhubri.modules.registration.controller;

import com.pointwest.bootcamp.eventhubri.modules.registration.dto.RegistrationRequestDto;
import com.pointwest.bootcamp.eventhubri.modules.registration.dto.RegistrationResponseDto;
import com.pointwest.bootcamp.eventhubri.modules.registration.dto.RegistrationStatusUpdateDto;
import com.pointwest.bootcamp.eventhubri.modules.registration.service.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/registrations")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping
    @PreAuthorize("hasAuthority('REGISTER_FOR_EVENT')")
    public ResponseEntity<RegistrationResponseDto> register(
            @Valid @RequestBody RegistrationRequestDto request,
            Authentication authentication) {
        RegistrationResponseDto response = registrationService.register(request.eventId(), authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/me")
    @PreAuthorize("hasAuthority('MANAGE_OWN_PROFILE')")
    public ResponseEntity<Page<RegistrationResponseDto>> listOwn(
            Authentication authentication,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(registrationService.listOwnRegistrations(authentication.getName(), pageable));
    }

    @GetMapping("/{registrationId}")
    @PreAuthorize("hasAuthority('MANAGE_OWN_PROFILE')")
    public ResponseEntity<RegistrationResponseDto> getOwn(
            @PathVariable Long registrationId,
            Authentication authentication) {
        return ResponseEntity.ok(registrationService.getOwnRegistration(registrationId, authentication.getName()));
    }

    @DeleteMapping("/{registrationId}")
    @PreAuthorize("hasAuthority('MANAGE_OWN_PROFILE') or hasAuthority('MANAGE_STAFF') or hasAuthority('MANAGE_EVENTS')")
    public ResponseEntity<Void> cancelOwn(
            @PathVariable Long registrationId,
            Authentication authentication) {
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("MANAGE_OWN_PROFILE"))) {
            registrationService.cancelOwnRegistration(registrationId, authentication.getName());
            return ResponseEntity.noContent().build();
        }

        registrationService.removeRegistrationForOrganizer(registrationId, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{registrationId}/organizer-remove")
    @PreAuthorize("hasAuthority('MANAGE_STAFF') or hasAuthority('MANAGE_EVENTS')")
    public ResponseEntity<Void> removeRegistrationForOrganizer(
            @PathVariable Long registrationId,
            Authentication authentication) {
        registrationService.removeRegistrationForOrganizer(registrationId, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{registrationId}/check-in")
    @PreAuthorize("hasAuthority('CHECK_IN_ATTENDEES')")
    public ResponseEntity<RegistrationResponseDto> checkIn(
            @PathVariable Long registrationId,
            Authentication authentication) {
        return ResponseEntity.ok(registrationService.checkInAttendee(registrationId, authentication.getName()));
    }

    @PatchMapping("/{registrationId}/status")
    @PreAuthorize("hasAuthority('CHECK_IN_ATTENDEES') or hasAuthority('MANAGE_EVENTS')")
    public ResponseEntity<RegistrationResponseDto> updateStatus(
            @PathVariable Long registrationId,
            @Valid @RequestBody RegistrationStatusUpdateDto request,
            Authentication authentication) {
        return ResponseEntity.ok(
                registrationService.updateStatus(registrationId, request.status(), authentication.getName()));
    }
}
