package com.pointwest.bootcamp.eventhubri.core.exception;

import com.pointwest.bootcamp.eventhubri.core.exception.enums.EventhubErrorCode;

public class ResourceNotFoundException extends EventhubException {
    public ResourceNotFoundException(String message) {
        super(EventhubErrorCode.RESOURCE_NOT_FOUND, message);
    }
}
