package com.pointwest.bootcamp.eventhubri.modules.event.exception;

import com.pointwest.bootcamp.eventhubri.core.exception.EventhubException;
import com.pointwest.bootcamp.eventhubri.core.exception.enums.EventhubErrorCode;

public class EventNotFoundException extends EventhubException{
    public EventNotFoundException(Long eventId){
        super("Event not found with ID: " + eventId, EventhubErrorCode.EVENT_NOT_FOUND.getErrorCode(), EventhubErrorCode.EVENT_NOT_FOUND.getHttpStatusCode());
    }
}
