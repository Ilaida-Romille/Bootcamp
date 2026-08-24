package com.pointwest.bootcamp.eventhubri.controller;

import com.pointwest.bootcamp.eventhubri.service.OrganizerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrganizerController.class)
public class OrganizerControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrganizerService organizerService;

    @Test
    public void testExportEvents() throws Exception {
        mockMvc.perform(get("/organizer/export"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "text/csv"))
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"events.csv\""));
    }

    @Test
    public void testImportEvents() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.csv", "text/csv", "title,capacity\nTech Conf,100".getBytes()
        );

        when(organizerService.importEvents(any())).thenReturn(List.of(1L, 2L));

        mockMvc.perform(multipart("/organizer/import").file(file))
                .andExpect(status().isCreated());
    }
}