package com.pointwest.bootcamp.eventhubri.core.exception.dto;

import java.time.Instant;

import com.pointwest.bootcamp.eventhubri.core.exception.enums.EventhubErrorCode;

public record ErrorResponse(
        EventhubErrorCode errorCode,
        String message,
        Instant timeStamp) {
}
