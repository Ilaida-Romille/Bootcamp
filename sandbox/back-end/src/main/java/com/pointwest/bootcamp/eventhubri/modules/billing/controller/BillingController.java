package com.pointwest.bootcamp.eventhubri.modules.billing.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.pointwest.bootcamp.eventhubri.modules.billing.dto.InvoiceResponseDto;
import com.pointwest.bootcamp.eventhubri.modules.billing.dto.PlatformRateRequestDto;
import com.pointwest.bootcamp.eventhubri.modules.billing.entity.PlatformRate;
import com.pointwest.bootcamp.eventhubri.modules.billing.service.BillingService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/billing")
@RequiredArgsConstructor
public class BillingController {

    private final BillingService billingService;

    @PutMapping("/rate")
    @PreAuthorize("hasAuthority('MANAGE_PLATFORM_RATES')")
    public ResponseEntity<PlatformRate> updateBaseRate(@Valid @RequestBody PlatformRateRequestDto request) {
        return ResponseEntity.ok(billingService.setBaseRate(request));
    }

    @GetMapping("/invoices")
    @PreAuthorize("hasAuthority('VIEW_ALL_INVOICES')")
    public ResponseEntity<List<InvoiceResponseDto>> getAllInvoices() {
        return ResponseEntity.ok(billingService.getAllInvoices());
    }

    @GetMapping("/invoices/monthly")
    @PreAuthorize("hasAuthority('VIEW_ALL_INVOICES')")
    public ResponseEntity<List<InvoiceResponseDto>> getInvoicesByMonth(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodStart,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodEnd) {
        return ResponseEntity.ok(billingService.getInvoicesByPeriod(periodStart, periodEnd));
    }

    @GetMapping("/invoices/organization/{organizationId}")
    @PreAuthorize("hasAuthority('VIEW_ALL_INVOICES')")
    public ResponseEntity<List<InvoiceResponseDto>> getInvoicesByOrganization(@PathVariable Long organizationId) {
        return ResponseEntity.ok(billingService.getInvoicesByOrganization(organizationId));
    }

    @PostMapping("/invoices/generate")
    @PreAuthorize("hasAuthority('MANAGE_PLATFORM_RATES')")
    public ResponseEntity<InvoiceResponseDto> generateInvoice(
            @RequestParam Long organizationId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodStart,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodEnd) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(billingService.generateInvoice(organizationId, periodStart, periodEnd));
    }

    @PostMapping("/invoices/generate/batch")
    @PreAuthorize("hasAuthority('MANAGE_PLATFORM_RATES')")
    public ResponseEntity<List<InvoiceResponseDto>> generateBatchInvoices(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodStart,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodEnd) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(billingService.generateBatchInvoices(periodStart, periodEnd));
    }

    @GetMapping("/invoices/{invoiceId}/export")
    @PreAuthorize("hasAuthority('VIEW_ALL_INVOICES')")
    public ResponseEntity<byte[]> exportInvoice(@PathVariable Long invoiceId) {
        byte[] pdf = billingService.exportInvoiceAsPdf(invoiceId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice-" + invoiceId + ".pdf")
                .body(pdf);
    }
}