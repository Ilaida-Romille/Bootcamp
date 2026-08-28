package com.pointwest.bootcamp.eventhubri.core.exception.dto;

import java.time.Instant;

public record ErrorResponse(
    int status,
    String errorCode,
    String message,
    Instant timestamp
) {}

