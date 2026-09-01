package com.pointwest.bootcamp.eventhubri.modules.registration.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.pointwest.bootcamp.eventhubri.modules.registration.dto.RegistrationResponseDto;
import com.pointwest.bootcamp.eventhubri.modules.registration.entity.RegistrationStatus;
import com.pointwest.bootcamp.eventhubri.modules.registration.service.RegistrationService;

@ExtendWith(MockitoExtension.class)
class EventRegistrationControllerTest {

    @Mock
    private RegistrationService registrationService;

    @InjectMocks
    private EventRegistrationController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void listForEvent_returnsPage() throws Exception {
        RegistrationResponseDto response = new RegistrationResponseDto(
                5L, 2L, "Spring Data Bootcamp", LocalDateTime.now(), LocalDateTime.now().plusHours(3),
                11L, "Lucy User", RegistrationStatus.CONFIRMED, null, LocalDateTime.now());
        Page<RegistrationResponseDto> page = new PageImpl<>(List.of(response), PageRequest.of(0, 20), 1);

        when(registrationService.listRegistrationsForEvent(eq(2L), eq(RegistrationStatus.CONFIRMED), eq("staff@example.com"), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/events/{eventId}/registrations", 2L)
                        .param("status", "CONFIRMED")
                        .principal(new UsernamePasswordAuthenticationToken("staff@example.com", "pw")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].eventTitle").value("Spring Data Bootcamp"));

        verify(registrationService).listRegistrationsForEvent(eq(2L), eq(RegistrationStatus.CONFIRMED), eq("staff@example.com"), any(Pageable.class));
    }

    @Test
    void listAttendeesPublic_returnsPage() throws Exception {
        RegistrationResponseDto response = new RegistrationResponseDto(
                9L, 4L, "AI for Product Teams", LocalDateTime.now(), LocalDateTime.now().plusHours(2),
                12L, "Noah User", RegistrationStatus.CONFIRMED, null, LocalDateTime.now());
        Page<RegistrationResponseDto> page = new PageImpl<>(List.of(response), PageRequest.of(0, 50), 1);

        when(registrationService.listConfirmedAttendeesForEvent(eq(4L), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/events/{eventId}/registrations/attendees", 4L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].attendeeName").value("Noah User"));

        verify(registrationService).listConfirmedAttendeesForEvent(eq(4L), any(Pageable.class));
    }
}
