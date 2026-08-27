package com.pointwest.bootcamp.eventhubri.modules.billing.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record PlatformRateRequestDto(

        @NotNull @DecimalMin(value = "0.0", inclusive = false, message = "Rate must be greater than zero") BigDecimal ratePerAttendee,

        @NotNull LocalDate effectiveStartDate,

        // Null means "open-ended" — still in effect until superseded.
        LocalDate effectiveEndDate) {
}
