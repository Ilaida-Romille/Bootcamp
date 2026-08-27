package com.pointwest.bootcamp.eventhubri.modules.billing.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.pointwest.bootcamp.eventhubri.modules.billing.entity.OrganizationInvoice;

public record InvoiceResponseDto(
        Long id,
        Long organizationId,
        String organizationName,
        String invoiceNumber,
        LocalDate billingPeriodStart,
        LocalDate billingPeriodEnd,
        Integer totalAttendeeCount,
        BigDecimal appliedRatePerAttendee,
        BigDecimal invoiceAmount,
        OrganizationInvoice.PaymentStatus paymentStatus,
        LocalDateTime issuedAt,
        LocalDate dueDate,
        List<InvoiceLineItem> items) {

    public record InvoiceLineItem(
            Long eventId,
            String eventTitle,
            Integer eventAttendeeCount,
            BigDecimal rateApplied,
            BigDecimal lineTotal) {
    }
}
