package com.pointwest.bootcamp.eventhubri.modules.registration.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pointwest.bootcamp.eventhubri.modules.registration.dto.RegistrationRequestDto;
import com.pointwest.bootcamp.eventhubri.modules.registration.dto.RegistrationResponseDto;
import com.pointwest.bootcamp.eventhubri.modules.registration.dto.RegistrationStatusUpdateDto;
import com.pointwest.bootcamp.eventhubri.modules.registration.entity.RegistrationStatus;
import com.pointwest.bootcamp.eventhubri.modules.registration.service.RegistrationService;

@ExtendWith(MockitoExtension.class)
class RegistrationControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private RegistrationService registrationService;

    @InjectMocks
    private RegistrationController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        objectMapper.findAndRegisterModules();
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void register_createsRegistration() throws Exception {
        RegistrationRequestDto request = new RegistrationRequestDto(8L);
        RegistrationResponseDto response = new RegistrationResponseDto(
                99L, 8L, "Spring Boot Intro", LocalDateTime.now(), LocalDateTime.now().plusHours(2),
                21L, "Jane User", RegistrationStatus.CONFIRMED, null, LocalDateTime.now());

        when(registrationService.register(eq(8L), eq("user@example.com"))).thenReturn(response);

        mockMvc.perform(post("/api/registrations")
                        .principal(new UsernamePasswordAuthenticationToken("user@example.com", "pw"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.eventId").value(8));

        verify(registrationService).register(8L, "user@example.com");
    }

    @Test
    void listOwn_returnsPage() throws Exception {
        RegistrationResponseDto response = new RegistrationResponseDto(
                10L, 3L, "Design Workshop", LocalDateTime.now(), LocalDateTime.now().plusHours(3),
                4L, "John Doe", RegistrationStatus.CONFIRMED, null, LocalDateTime.now());
        Page<RegistrationResponseDto> page = new PageImpl<>(List.of(response), PageRequest.of(0, 20), 1);

        when(registrationService.listOwnRegistrations(eq("user@example.com"), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/registrations/me")
                        .principal(new UsernamePasswordAuthenticationToken("user@example.com", "pw")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].eventTitle").value("Design Workshop"));

        verify(registrationService).listOwnRegistrations(eq("user@example.com"), any(Pageable.class));
    }

    @Test
    void getOwn_returnsRegistration() throws Exception {
        RegistrationResponseDto response = new RegistrationResponseDto(
                12L, 6L, "Leadership Forum", LocalDateTime.now(), LocalDateTime.now().plusHours(2),
                9L, "Ada User", RegistrationStatus.CONFIRMED, null, LocalDateTime.now());

        when(registrationService.getOwnRegistration(12L, "user@example.com")).thenReturn(response);

        mockMvc.perform(get("/api/registrations/{registrationId}", 12L)
                        .principal(new UsernamePasswordAuthenticationToken("user@example.com", "pw")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventTitle").value("Leadership Forum"));

        verify(registrationService).getOwnRegistration(12L, "user@example.com");
    }

    @Test
    void cancelOwn_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/registrations/{registrationId}", 12L)
                        .principal(new UsernamePasswordAuthenticationToken(
                                "user@example.com",
                                "pw",
                                java.util.List.of(new org.springframework.security.core.authority.SimpleGrantedAuthority("MANAGE_OWN_PROFILE")))))
                .andExpect(status().isNoContent());

        verify(registrationService).cancelOwnRegistration(12L, "user@example.com");
    }

    @Test
    void updateStatus_returnsUpdatedRegistration() throws Exception {
        RegistrationStatusUpdateDto request = new RegistrationStatusUpdateDto(RegistrationStatus.CANCELLED);
        RegistrationResponseDto response = new RegistrationResponseDto(
                14L, 7L, "Networking Expo", LocalDateTime.now(), LocalDateTime.now().plusHours(4),
                15L, "Ruth User", RegistrationStatus.CANCELLED, null, LocalDateTime.now());

        when(registrationService.updateStatus(14L, RegistrationStatus.CANCELLED, "staff@example.com")).thenReturn(response);

        mockMvc.perform(patch("/api/registrations/{registrationId}/status", 14L)
                        .principal(new UsernamePasswordAuthenticationToken("staff@example.com", "pw"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));

        verify(registrationService).updateStatus(14L, RegistrationStatus.CANCELLED, "staff@example.com");
    }
}
