package com.pointwest.bootcamp.eventhubri.modules.communication.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pointwest.bootcamp.eventhubri.modules.communication.dto.EmailSendRequestDto;
import com.pointwest.bootcamp.eventhubri.modules.communication.dto.NotificationLogResponseDto;
import com.pointwest.bootcamp.eventhubri.modules.communication.service.EmailNotificationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final EmailNotificationService emailNotificationService;

    @PostMapping("/email")
    @PreAuthorize("hasAuthority('SEND_NOTIFICATIONS')")
    public ResponseEntity<NotificationLogResponseDto> sendEventEmails(
            @Valid @RequestBody EmailSendRequestDto request,
            Authentication authentication) {

        NotificationLogResponseDto response = emailNotificationService.sendBroadcastEmail(request,
                authentication.getName());
        return ResponseEntity.ok(response);
    }
}
