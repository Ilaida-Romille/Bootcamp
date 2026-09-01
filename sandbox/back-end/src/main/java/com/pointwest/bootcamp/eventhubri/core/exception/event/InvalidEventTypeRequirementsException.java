package com.pointwest.bootcamp.eventhubri.core.exception.event;

import com.pointwest.bootcamp.eventhubri.core.exception.EventhubException;
import com.pointwest.bootcamp.eventhubri.core.exception.enums.EventhubErrorCode;

public class InvalidEventTypeRequirementsException extends EventhubException {
    public InvalidEventTypeRequirementsException(String message) {
        super(EventhubErrorCode.INVALID_EVENT_TYPE_REQUIREMENTS, message);
    }
}
