package com.pointwest.bootcamp.eventhubri.core.exception;

import com.pointwest.bootcamp.eventhubri.core.exception.enums.EventhubErrorCode;

import lombok.Getter;

@Getter
public abstract class EventhubException extends RuntimeException {
    private final EventhubErrorCode errorCode;

    public EventhubException(EventhubErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public EventhubException(EventhubErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

}
