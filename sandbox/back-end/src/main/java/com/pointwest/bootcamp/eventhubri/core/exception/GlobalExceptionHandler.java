package com.pointwest.bootcamp.eventhubri.core.exception;

import java.time.Instant;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.pointwest.bootcamp.eventhubri.core.exception.dto.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(EventhubException.class)
    public ResponseEntity<ErrorResponse> handleEventhubException(EventhubException ex){
        ErrorResponse errorResponse = new ErrorResponse(
            ex.getHttpStatusCode(),
            ex.getErrorCode(),
            ex.getMessage(),
            Instant.now()
        );

        return ResponseEntity.status(ex.getHttpStatusCode()).body(errorResponse);
    }
}
