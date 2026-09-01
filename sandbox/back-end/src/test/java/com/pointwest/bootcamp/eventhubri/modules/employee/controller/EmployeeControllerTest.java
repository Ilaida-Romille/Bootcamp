package com.pointwest.bootcamp.eventhubri.modules.employee.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pointwest.bootcamp.eventhubri.modules.account.entity.AppUser;
import com.pointwest.bootcamp.eventhubri.modules.account.entity.Organization;
import com.pointwest.bootcamp.eventhubri.modules.account.entity.Role;
import com.pointwest.bootcamp.eventhubri.modules.auth.security.CustomUserDetails;
import com.pointwest.bootcamp.eventhubri.modules.employee.dto.EmployeeResponseDto;
import com.pointwest.bootcamp.eventhubri.modules.employee.dto.RegisteredEventSummaryDto;
import com.pointwest.bootcamp.eventhubri.modules.employee.dto.UpdateEmployeeRequestDto;
import com.pointwest.bootcamp.eventhubri.modules.employee.service.EmployeeService;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        objectMapper.findAndRegisterModules();
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(new AuthenticationPrincipalArgumentResolver())
                .build();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void getEmployees_returnsList() throws Exception {
        EmployeeResponseDto response = new EmployeeResponseDto(
                5L,
                "Kelly",
                "Hughes",
                "kelly@acme.com",
                "Acme Events",
                "avatar.png",
                Role.ORGANIZER_STAFF.name(),
                List.of(new RegisteredEventSummaryDto(7L, 9L, "Spring Summit", true)));

        when(employeeService.getEmployees(10L)).thenReturn(List.of(response));
        setAuthenticatedUser(10L, "manager@acme.com");

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("kelly@acme.com"));

        verify(employeeService).getEmployees(10L);
    }

    @Test
    void getEmployee_returnsEmployee() throws Exception {
        EmployeeResponseDto response = new EmployeeResponseDto(
                5L,
                "Kelly",
                "Hughes",
                "kelly@acme.com",
                "Acme Events",
                "avatar.png",
                Role.ORGANIZER_STAFF.name(),
                List.of());

        when(employeeService.getEmployee(10L, 5L)).thenReturn(response);
        setAuthenticatedUser(10L, "manager@acme.com");

        mockMvc.perform(get("/api/employees/{id}", 5L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Kelly"));

        verify(employeeService).getEmployee(10L, 5L);
    }

    @Test
    void updateEmployee_returnsUpdatedEmployee() throws Exception {
        UpdateEmployeeRequestDto request = new UpdateEmployeeRequestDto("Kelly", "Hughes", "kelly@acme.com", "Acme Events", "avatar.png");
        EmployeeResponseDto response = new EmployeeResponseDto(
                5L,
                "Kelly",
                "Hughes",
                "kelly@acme.com",
                "Acme Events",
                "avatar.png",
                Role.ORGANIZER_STAFF.name(),
                List.of());

        when(employeeService.updateEmployee(eq(10L), eq(5L), any(UpdateEmployeeRequestDto.class))).thenReturn(response);
        setAuthenticatedUser(10L, "manager@acme.com");

        mockMvc.perform(put("/api/employees/{id}", 5L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("kelly@acme.com"));

        verify(employeeService).updateEmployee(eq(10L), eq(5L), any(UpdateEmployeeRequestDto.class));
    }

    @Test
    void patchEmployee_returnsPatchedEmployee() throws Exception {
        UpdateEmployeeRequestDto request = new UpdateEmployeeRequestDto("Kelly", null, null, null, null);
        EmployeeResponseDto response = new EmployeeResponseDto(
                5L,
                "Kelly",
                "Hughes",
                "kelly@acme.com",
                "Acme Events",
                "avatar.png",
                Role.ORGANIZER_STAFF.name(),
                List.of());

        when(employeeService.patchEmployee(eq(10L), eq(5L), any(UpdateEmployeeRequestDto.class))).thenReturn(response);
        setAuthenticatedUser(10L, "manager@acme.com");

        mockMvc.perform(patch("/api/employees/{id}", 5L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Kelly"));

        verify(employeeService).patchEmployee(eq(10L), eq(5L), any(UpdateEmployeeRequestDto.class));
    }

    @Test
    void deleteEmployee_returnsNoContent() throws Exception {
        setAuthenticatedUser(10L, "manager@acme.com");

        mockMvc.perform(delete("/api/employees/{id}", 5L))
                .andExpect(status().isNoContent());

        verify(employeeService).deleteEmployee(10L, 5L);
    }

    private void setAuthenticatedUser(Long organizationId, String email) {
        Organization organization = Organization.builder()
                .id(organizationId)
                .companyName("Acme Events")
                .primaryContactEmail("primary@acme.com")
                .status(Organization.Status.ACTIVE)
                .build();

        AppUser user = AppUser.builder()
                .id(99L)
                .email(email)
                .organization(organization)
                .firstName("Manager")
                .lastName("User")
                .role(Role.ORGANIZER_STAFF)
                .status(AppUser.Status.ACTIVE)
                .passwordHash("hashed")
                .build();

        SecurityContextHolder.getContext().setAuthentication(
                new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                        new CustomUserDetails(user), null, new java.util.ArrayList<>()));
    }
}
