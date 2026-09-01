package com.pointwest.bootcamp.eventhubri.core.exception.communication;

import com.pointwest.bootcamp.eventhubri.core.exception.EventhubException;
import com.pointwest.bootcamp.eventhubri.core.exception.enums.EventhubErrorCode;

public class NoRegistrationsFoundException extends EventhubException {
    public NoRegistrationsFoundException(Long eventId) {
        super(EventhubErrorCode.NO_REGISTRATIONS_FOR_EVENT, "No registrations found for this event: " + eventId);
    }
}
