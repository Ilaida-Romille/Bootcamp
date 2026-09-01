package com.pointwest.bootcamp.eventhubri.modules.billing.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pointwest.bootcamp.eventhubri.modules.account.entity.Organization;
import com.pointwest.bootcamp.eventhubri.modules.billing.dto.InvoiceResponseDto;
import com.pointwest.bootcamp.eventhubri.modules.billing.dto.PlatformRateRequestDto;
import com.pointwest.bootcamp.eventhubri.modules.billing.entity.OrganizationInvoice;
import com.pointwest.bootcamp.eventhubri.modules.billing.entity.PlatformRate;
import com.pointwest.bootcamp.eventhubri.modules.billing.service.BillingService;

@ExtendWith(MockitoExtension.class)
class BillingControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private BillingService billingService;

    @InjectMocks
    private BillingController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        objectMapper.findAndRegisterModules();
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void updateBaseRate_returnsPlatformRate() throws Exception {
        PlatformRateRequestDto request = new PlatformRateRequestDto(
                new BigDecimal("250.00"),
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 12, 31));

        PlatformRate response = PlatformRate.builder()
                .id(1L)
                .ratePerAttendee(new BigDecimal("250.00"))
                .effectiveStartDate(LocalDate.of(2026, 1, 1))
                .effectiveEndDate(LocalDate.of(2026, 12, 31))
                .isActive(true)
                .build();

        when(billingService.setBaseRate(request)).thenReturn(response);

        mockMvc.perform(put("/api/billing/rate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ratePerAttendee").value(250.00));

        verify(billingService).setBaseRate(request);
    }

    @Test
    void getAllInvoices_returnsList() throws Exception {
        InvoiceResponseDto response = new InvoiceResponseDto(
                1L,
                10L,
                "Acme Events",
                "primary@acme.com",
                "INV-2026-001",
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 1, 31),
                5,
                new BigDecimal("250.00"),
                new BigDecimal("1250.00"),
                OrganizationInvoice.PaymentStatus.UNPAID,
                LocalDateTime.of(2026, 1, 31, 9, 0),
                LocalDate.of(2026, 2, 15),
                List.of(new InvoiceResponseDto.InvoiceLineItem(5L, "Spring Summit", 5, new BigDecimal("250.00"), new BigDecimal("1250.00"))));

        when(billingService.getAllInvoices()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/billing/invoices"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].invoiceNumber").value("INV-2026-001"));

        verify(billingService).getAllInvoices();
    }

    @Test
    void getInvoicesByMonth_returnsInvoicesForPeriod() throws Exception {
        InvoiceResponseDto response = new InvoiceResponseDto(
                2L,
                12L,
                "EventCo",
                "ops@eventco.com",
                "INV-2026-002",
                LocalDate.of(2026, 2, 1),
                LocalDate.of(2026, 2, 28),
                3,
                new BigDecimal("300.00"),
                new BigDecimal("900.00"),
                OrganizationInvoice.PaymentStatus.PAID,
                LocalDateTime.of(2026, 2, 28, 13, 30),
                LocalDate.of(2026, 3, 15),
                List.of());

        when(billingService.getInvoicesByPeriod(LocalDate.of(2026, 2, 1), LocalDate.of(2026, 2, 28))).thenReturn(List.of(response));

        mockMvc.perform(get("/api/billing/invoices/monthly")
                        .param("periodStart", "2026-02-01")
                        .param("periodEnd", "2026-02-28"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].organizationName").value("EventCo"));

        verify(billingService).getInvoicesByPeriod(LocalDate.of(2026, 2, 1), LocalDate.of(2026, 2, 28));
    }

    @Test
    void generateInvoice_returnsCreatedInvoice() throws Exception {
        InvoiceResponseDto response = new InvoiceResponseDto(
                3L,
                20L,
                "Bright Event",
                "finance@brightevent.com",
                "INV-2026-003",
                LocalDate.of(2026, 3, 1),
                LocalDate.of(2026, 3, 31),
                10,
                new BigDecimal("200.00"),
                new BigDecimal("2000.00"),
                OrganizationInvoice.PaymentStatus.UNPAID,
                LocalDateTime.of(2026, 3, 31, 8, 15),
                LocalDate.of(2026, 4, 30),
                List.of());

        when(billingService.generateInvoice(20L, LocalDate.of(2026, 3, 1), LocalDate.of(2026, 3, 31))).thenReturn(response);

        mockMvc.perform(post("/api/billing/invoices/generate")
                        .param("organizationId", "20")
                        .param("periodStart", "2026-03-01")
                        .param("periodEnd", "2026-03-31"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.invoiceNumber").value("INV-2026-003"));

        verify(billingService).generateInvoice(20L, LocalDate.of(2026, 3, 1), LocalDate.of(2026, 3, 31));
    }

    @Test
    void generateBatchInvoices_returnsCreatedInvoices() throws Exception {
        InvoiceResponseDto response = new InvoiceResponseDto(
                4L,
                21L,
                "North Star",
                "billing@northstar.com",
                "INV-2026-004",
                LocalDate.of(2026, 4, 1),
                LocalDate.of(2026, 4, 30),
                8,
                new BigDecimal("180.00"),
                new BigDecimal("1440.00"),
                OrganizationInvoice.PaymentStatus.UNPAID,
                LocalDateTime.of(2026, 4, 30, 10, 0),
                LocalDate.of(2026, 5, 30),
                List.of());

        when(billingService.generateBatchInvoices(LocalDate.of(2026, 4, 1), LocalDate.of(2026, 4, 30))).thenReturn(List.of(response));

        mockMvc.perform(post("/api/billing/invoices/generate/batch")
                        .param("periodStart", "2026-04-01")
                        .param("periodEnd", "2026-04-30"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].organizationName").value("North Star"));

        verify(billingService).generateBatchInvoices(LocalDate.of(2026, 4, 1), LocalDate.of(2026, 4, 30));
    }

    @Test
    void exportInvoice_returnsPdfContent() throws Exception {
        byte[] payload = "pdf-content".getBytes();
        when(billingService.exportInvoiceAsPdf(15L)).thenReturn(payload);

        mockMvc.perform(get("/api/billing/invoices/{invoiceId}/export", 15L))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=invoice-15.pdf"))
                .andExpect(header().string("Content-Type", "application/pdf"));

        verify(billingService).exportInvoiceAsPdf(15L);
    }
}
