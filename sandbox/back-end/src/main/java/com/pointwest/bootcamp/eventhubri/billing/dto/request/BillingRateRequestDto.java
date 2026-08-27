package com.pointwest.bootcamp.eventhubri.billing.dto.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record BillingRateRequestDto(
        Long organizerId,
        @NotNull @Positive BigDecimal ratePerAttendee,
        @NotNull @Size(min = 3, max = 10) String currency,
        @NotNull LocalDateTime effectiveFrom,
        LocalDateTime effectiveTo
) {
}
