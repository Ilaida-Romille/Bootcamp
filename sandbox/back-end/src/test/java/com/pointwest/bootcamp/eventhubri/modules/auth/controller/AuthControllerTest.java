package com.pointwest.bootcamp.eventhubri.modules.auth.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pointwest.bootcamp.eventhubri.modules.account.dto.UserResponseDto;
import com.pointwest.bootcamp.eventhubri.modules.account.entity.AppUser;
import com.pointwest.bootcamp.eventhubri.modules.account.entity.Role;
import com.pointwest.bootcamp.eventhubri.modules.auth.dto.LoginRequestDto;
import com.pointwest.bootcamp.eventhubri.modules.auth.dto.PublicOrganizationDto;
import com.pointwest.bootcamp.eventhubri.modules.auth.dto.RegisterAttendeeRequestDto;
import com.pointwest.bootcamp.eventhubri.modules.auth.dto.RegisterOrganizerRequestDto;
import com.pointwest.bootcamp.eventhubri.modules.auth.service.AuthService;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        objectMapper.findAndRegisterModules();
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void register_returnsCreated() throws Exception {
        RegisterOrganizerRequestDto request = new RegisterOrganizerRequestDto(
                "organizer@example.com",
                "secret123",
                "Jane",
                "Doe",
                "Acme Events",
                "primary@example.com",
                "09123456789");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(authService).registerOrganizer(request);
    }

    @Test
    void registerAttendee_returnsCreated() throws Exception {
        RegisterAttendeeRequestDto request = new RegisterAttendeeRequestDto(
                "attendee@example.com",
                "secret123",
                "Mary",
                "Smith",
                10L);

        mockMvc.perform(post("/api/auth/register/attendee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(authService).registerAttendee(request);
    }

    @Test
    void getPublicOrganizations_returnsOrganizations() throws Exception {
        List<PublicOrganizationDto> organizations = List.of(new PublicOrganizationDto(1L, "Acme Events"));
        when(authService.getPublicOrganizations()).thenReturn(organizations);

        mockMvc.perform(get("/api/auth/organizations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].companyName").value("Acme Events"));

        verify(authService).getPublicOrganizations();
    }

    @Test
    void login_returnsTokenAndSetsRefreshCookie() throws Exception {
        LoginRequestDto request = new LoginRequestDto("user@example.com", "secret123");
        when(authService.login(any(LoginRequestDto.class)))
                .thenReturn(new AuthService.AuthResult("access-token", 3600L, "refresh-token"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access-token"))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("eventhub_refresh=refresh-token")));

        verify(authService).login(any(LoginRequestDto.class));
    }

    @Test
    void refresh_returnsTokenAndSetsRefreshCookie() throws Exception {
        when(authService.refresh("refresh-token"))
                .thenReturn(new AuthService.AuthResult("new-access-token", 3600L, "replacement-token"));

        mockMvc.perform(post("/api/auth/refresh").cookie(new jakarta.servlet.http.Cookie("eventhub_refresh", "refresh-token")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("new-access-token"))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("eventhub_refresh=replacement-token")));

        verify(authService).refresh("refresh-token");
    }

    @Test
    void refresh_withoutCookie_returnsUnauthorized() throws Exception {
        mockMvc.perform(post("/api/auth/refresh"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void logout_returnsNoContentAndClearsCookie() throws Exception {
        mockMvc.perform(post("/api/auth/logout").cookie(new jakarta.servlet.http.Cookie("eventhub_refresh", "refresh-token")))
                .andExpect(status().isNoContent())
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("eventhub_refresh=;")));

        verify(authService).logout("refresh-token");
    }
}
