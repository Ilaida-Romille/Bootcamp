package com.pointwest.bootcamp.eventhubri.core.exception;

public class AccessDeniedOperationException extends RuntimeException {
    public AccessDeniedOperationException(String message) {
        super(message);
    }
}
