package com.pointwest.bootcamp.eventhubri.modules.account.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pointwest.bootcamp.eventhubri.modules.account.dto.OrganizerResponseDto;
import com.pointwest.bootcamp.eventhubri.modules.account.dto.UpdateOrganizerDto;
import com.pointwest.bootcamp.eventhubri.modules.account.entity.AppUser;
import com.pointwest.bootcamp.eventhubri.modules.account.entity.Organization;
import com.pointwest.bootcamp.eventhubri.modules.account.service.OrganizerManagementService;

@ExtendWith(MockitoExtension.class)
class PlatformOwnerOrganizerControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private OrganizerManagementService organizerManagementService;

    @InjectMocks
    private PlatformOwnerOrganizerController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        objectMapper.findAndRegisterModules();
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void getOrganizers_returnsPage() throws Exception {
        OrganizerResponseDto organizer = new OrganizerResponseDto(
                1L,
                "organizer@example.com",
                "Jane",
                "Doe",
                "Acme Events",
                AppUser.Status.ACTIVE,
                10L,
                "Acme Events",
                "primary@example.com",
                "09123456789",
                Organization.Status.ACTIVE,
                3L);

        Page<OrganizerResponseDto> page = new PageImpl<>(List.of(organizer), PageRequest.of(0, 10), 1);
        when(organizerManagementService.getOrganizers(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/platform-owner/organizers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].email").value("organizer@example.com"))
                .andExpect(jsonPath("$.content[0].company").value("Acme Events"));

        verify(organizerManagementService).getOrganizers(any(Pageable.class));
    }

    @Test
    void searchOrganizers_returnsPage() throws Exception {
        OrganizerResponseDto organizer = new OrganizerResponseDto(
                2L,
                "search@example.com",
                "John",
                "Smith",
                "EventCo",
                AppUser.Status.ACTIVE,
                11L,
                "EventCo",
                "search@eventco.com",
                "09111111111",
                Organization.Status.PENDING,
                5L);

        Page<OrganizerResponseDto> page = new PageImpl<>(List.of(organizer), PageRequest.of(0, 10), 1);
        when(organizerManagementService.searchOrganizers(org.mockito.ArgumentMatchers.eq("event"), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/platform-owner/organizers/search").param("query", "event"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].firstName").value("John"));

        verify(organizerManagementService).searchOrganizers(org.mockito.ArgumentMatchers.eq("event"), any(Pageable.class));
    }

    @Test
    void getOrganizerById_returnsOrganizer() throws Exception {
        OrganizerResponseDto organizer = new OrganizerResponseDto(
                3L,
                "detail@example.com",
                "Mary",
                "Jones",
                "Bright Event",
                AppUser.Status.ACTIVE,
                12L,
                "Bright Event",
                "mary@brightevent.com",
                "09121212121",
                Organization.Status.ACTIVE,
                7L);

        when(organizerManagementService.getOrganizerById(3L)).thenReturn(organizer);

        mockMvc.perform(get("/api/platform-owner/organizers/{id}", 3L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("detail@example.com"));

        verify(organizerManagementService).getOrganizerById(3L);
    }

    @Test
    void updateOrganizer_returnsUpdatedOrganizer() throws Exception {
        UpdateOrganizerDto updateDto = new UpdateOrganizerDto("Updated", "Name", "Updated Company", "Updated Org");
        OrganizerResponseDto organizer = new OrganizerResponseDto(
                4L,
                "updated@example.com",
                "Updated",
                "Name",
                "Updated Company",
                AppUser.Status.ACTIVE,
                13L,
                "Updated Org",
                "updated@updatedcompany.com",
                "09199999999",
                Organization.Status.ACTIVE,
                2L);

        when(organizerManagementService.updateOrganizer(4L, updateDto)).thenReturn(organizer);

        mockMvc.perform(put("/api/platform-owner/organizers/{id}", 4L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.company").value("Updated Company"));

        verify(organizerManagementService).updateOrganizer(4L, updateDto);
    }

    @Test
    void updateStatus_returnsUpdatedOrganizer() throws Exception {
        OrganizerResponseDto organizer = new OrganizerResponseDto(
                5L,
                "status@example.com",
                "Status",
                "Checker",
                "Status Company",
                AppUser.Status.INACTIVE,
                14L,
                "Status Company",
                "status@statuscompany.com",
                "09155555555",
                Organization.Status.ACTIVE,
                1L);

        when(organizerManagementService.updateUserStatus(5L, AppUser.Status.INACTIVE)).thenReturn(organizer);

        mockMvc.perform(patch("/api/platform-owner/organizers/{id}/status", 5L)
                        .param("status", "INACTIVE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userStatus").value("INACTIVE"));

        verify(organizerManagementService).updateUserStatus(5L, AppUser.Status.INACTIVE);
    }

    @Test
    void deleteOrganizer_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/platform-owner/organizers/{id}", 6L))
                .andExpect(status().isNoContent());

        verify(organizerManagementService).deleteOrganizer(6L);
    }
}
