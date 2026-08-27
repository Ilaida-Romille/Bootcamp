package com.pointwest.bootcamp.eventhubri.billing.dto.response;

import java.math.BigDecimal;

public record InvoiceItemResponseDto(
        Long id,
        Long eventId,
        String eventTitleSnapshot,
        Integer attendeeCount,
        BigDecimal rateApplied,
        BigDecimal lineTotal
) {
}
