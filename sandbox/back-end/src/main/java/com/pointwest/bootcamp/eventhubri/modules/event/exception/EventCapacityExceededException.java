package com.pointwest.bootcamp.eventhubri.modules.event.exception;

import com.pointwest.bootcamp.eventhubri.core.exception.EventhubException;
import com.pointwest.bootcamp.eventhubri.core.exception.enums.EventhubErrorCode;

public class EventCapacityExceededException extends EventhubException{

    public EventCapacityExceededException(Long eventId){
        super("Event with ID " + eventId + " has reached its maximum capacity", EventhubErrorCode.EVENT_CAPACITY_EXCEEDED.getErrorCode(), EventhubErrorCode.EVENT_CAPACITY_EXCEEDED.getHttpStatusCode());
    }
    
}
