package com.pointwest.bootcamp.eventhubri.core.exception;

import com.pointwest.bootcamp.eventhubri.core.exception.enums.EventhubErrorCode;

public class AccessDeniedOperationException extends EventhubException {
    public AccessDeniedOperationException(String message) {
        super(EventhubErrorCode.ACCESS_DENIED, message);
    }
}
