package com.pointwest.bootcamp.eventhubri.core.exception.event;

import com.pointwest.bootcamp.eventhubri.core.exception.EventhubException;
import com.pointwest.bootcamp.eventhubri.core.exception.enums.EventhubErrorCode;

public class EventNotFoundException extends EventhubException {
    public EventNotFoundException(Long eventId) {
        super(EventhubErrorCode.EVENT_NOT_FOUND, "Event of id: " + eventId + " not found");
    }
}
