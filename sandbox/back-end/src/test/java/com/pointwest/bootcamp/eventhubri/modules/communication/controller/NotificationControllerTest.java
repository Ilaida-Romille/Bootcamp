package com.pointwest.bootcamp.eventhubri.modules.communication.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pointwest.bootcamp.eventhubri.modules.communication.dto.EmailSendRequestDto;
import com.pointwest.bootcamp.eventhubri.modules.communication.dto.NotificationLogResponseDto;
import com.pointwest.bootcamp.eventhubri.modules.communication.entity.DeliveryStatus;
import com.pointwest.bootcamp.eventhubri.modules.communication.service.EmailNotificationService;

@ExtendWith(MockitoExtension.class)
class NotificationControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private EmailNotificationService emailNotificationService;

    @InjectMocks
    private NotificationController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        objectMapper.findAndRegisterModules();
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void sendEventEmails_returnsNotificationLog() throws Exception {
        EmailSendRequestDto request = new EmailSendRequestDto(7L, null, "Event reminder", "Hello there");
        NotificationLogResponseDto response = new NotificationLogResponseDto(
                99L,
                7L,
                1L,
                "Organizer Name",
                null,
                "EMAIL",
                "Event reminder",
                "Hello there",
                LocalDateTime.of(2026, 7, 2, 9, 15),
                DeliveryStatus.SENT);

        when(emailNotificationService.sendBroadcastEmail(any(EmailSendRequestDto.class), eq("organizer@example.com")))
                .thenReturn(response);

        mockMvc.perform(post("/api/notifications/email")
                        .principal(new UsernamePasswordAuthenticationToken("organizer@example.com", "pass"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.subject").value("Event reminder"));

        verify(emailNotificationService).sendBroadcastEmail(any(EmailSendRequestDto.class), eq("organizer@example.com"));
    }
}
