package com.pointwest.bootcamp.eventhubri.core.exception;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.pointwest.bootcamp.eventhubri.core.exception.dto.ErrorResponse;
import com.pointwest.bootcamp.eventhubri.core.exception.enums.EventhubErrorCode;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EventhubException.class)
    public ResponseEntity<ErrorResponse> handleEventhubException(EventhubException ex) {
        EventhubErrorCode errorCode = ex.getErrorCode();

        ErrorResponse response = new ErrorResponse(
                errorCode,
                ex.getMessage(),
                Instant.now());

        return ResponseEntity.status(errorCode.getHttpStatusCode()).body(response);
    }
}
