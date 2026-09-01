package com.pointwest.bootcamp.eventhubri.modules.platform.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pointwest.bootcamp.eventhubri.modules.platform.dto.PlatformOwnerDashboardDto;
import com.pointwest.bootcamp.eventhubri.modules.platform.service.PlatformOwnerDashboardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/platform-owner")
@RequiredArgsConstructor
public class PlatformOwnerDashboardController {

    private final PlatformOwnerDashboardService platformOwnerDashboardService;

    @GetMapping("/dashboard")
    @PreAuthorize("hasAuthority('VIEW_ALL_ORGANIZATIONS')")
    public ResponseEntity<PlatformOwnerDashboardDto> getDashboardMetrics() {
        return ResponseEntity.ok(platformOwnerDashboardService.getDashboardMetrics());
    }
}