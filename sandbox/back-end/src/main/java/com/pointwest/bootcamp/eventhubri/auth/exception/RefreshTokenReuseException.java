package com.pointwest.bootcamp.eventhubri.auth.exception;

public class RefreshTokenReuseException extends RuntimeException {
    public RefreshTokenReuseException(String message) {
        super(message);
    }
}
