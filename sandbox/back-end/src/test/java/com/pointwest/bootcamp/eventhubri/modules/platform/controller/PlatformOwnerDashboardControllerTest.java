package com.pointwest.bootcamp.eventhubri.modules.platform.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.pointwest.bootcamp.eventhubri.modules.platform.dto.MonthlyEventCountDto;
import com.pointwest.bootcamp.eventhubri.modules.platform.dto.PlatformOwnerDashboardDto;
import com.pointwest.bootcamp.eventhubri.modules.platform.service.PlatformOwnerDashboardService;

@ExtendWith(MockitoExtension.class)
class PlatformOwnerDashboardControllerTest {

    @Mock
    private PlatformOwnerDashboardService platformOwnerDashboardService;

    @InjectMocks
    private PlatformOwnerDashboardController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void getDashboardMetrics_returnsDashboard() throws Exception {
        PlatformOwnerDashboardDto response = new PlatformOwnerDashboardDto(
                12,
                35,
                List.of(new MonthlyEventCountDto("Jan", 3), new MonthlyEventCountDto("Feb", 5)));

        when(platformOwnerDashboardService.getDashboardMetrics()).thenReturn(response);

        mockMvc.perform(get("/api/platform-owner/dashboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.organizersCount").value(12))
                .andExpect(jsonPath("$.eventsCount").value(35))
                .andExpect(jsonPath("$.monthlyEventsData[0].label").value("Jan"));

        verify(platformOwnerDashboardService).getDashboardMetrics();
    }
}
