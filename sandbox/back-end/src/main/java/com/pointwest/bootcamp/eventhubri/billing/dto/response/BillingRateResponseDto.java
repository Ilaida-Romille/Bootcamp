package com.pointwest.bootcamp.eventhubri.billing.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BillingRateResponseDto(
        Long id,
        Long organizerId,
        BigDecimal ratePerAttendee,
        String currency,
        LocalDateTime effectiveFrom,
        LocalDateTime effectiveTo
) {
}
