package com.pointwest.bootcamp.eventhubri.modules.account.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.pointwest.bootcamp.eventhubri.modules.account.dto.OrganizerResponseDto;
import com.pointwest.bootcamp.eventhubri.modules.account.dto.UpdateOrganizerDto;
import com.pointwest.bootcamp.eventhubri.modules.account.entity.AppUser;
import com.pointwest.bootcamp.eventhubri.modules.account.service.OrganizerManagementService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/platform-owner/organizers")
@RequiredArgsConstructor
public class PlatformOwnerOrganizerController {

    private final OrganizerManagementService organizerManagementService;

    @GetMapping
    @PreAuthorize("hasAuthority('VIEW_ALL_ORGANIZATIONS')")
    public ResponseEntity<Page<OrganizerResponseDto>> getOrganizers(
            @PageableDefault(size = 500, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(organizerManagementService.getOrganizers(pageable));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('VIEW_ALL_ORGANIZATIONS')")
    public ResponseEntity<Page<OrganizerResponseDto>> searchOrganizers(
            @RequestParam String query,
            @PageableDefault(size = 500, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(organizerManagementService.searchOrganizers(query, pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('VIEW_ALL_ORGANIZATIONS')")
    public ResponseEntity<OrganizerResponseDto> getOrganizerById(@PathVariable Long id) {
        return ResponseEntity.ok(organizerManagementService.getOrganizerById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('MANAGE_ORGANIZATIONS')")
    public ResponseEntity<OrganizerResponseDto> updateOrganizer(
            @PathVariable Long id,
            @Valid @RequestBody UpdateOrganizerDto dto) {
        return ResponseEntity.ok(organizerManagementService.updateOrganizer(id, dto));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAuthority('MANAGE_ORGANIZATIONS')")
    public ResponseEntity<OrganizerResponseDto> updateStatus(
            @PathVariable Long id,
            @RequestParam AppUser.Status status) {
        return ResponseEntity.ok(organizerManagementService.updateUserStatus(id, status));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('MANAGE_ORGANIZATIONS')")
    public ResponseEntity<Void> deleteOrganizer(@PathVariable Long id) {
        organizerManagementService.deleteOrganizer(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
