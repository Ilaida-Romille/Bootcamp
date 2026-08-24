package com.pointwest.bootcamp.eventhubri.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.pointwest.bootcamp.eventhubri.dto.ErrorResponseDto;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponseDto> handleAppException(AppException exception) {
        exception.printStackTrace();
        return ResponseEntity.status(exception.getStatusCode()).body(new ErrorResponseDto(exception.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseDto> handleOtherException(Exception exception) {
        exception.printStackTrace();
        return ResponseEntity.status(500).body(new ErrorResponseDto(exception.getMessage()));
    }

}
