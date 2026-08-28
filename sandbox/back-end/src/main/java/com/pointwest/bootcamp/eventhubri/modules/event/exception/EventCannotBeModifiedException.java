package com.pointwest.bootcamp.eventhubri.modules.event.exception;

import com.pointwest.bootcamp.eventhubri.core.exception.EventhubException;
import com.pointwest.bootcamp.eventhubri.core.exception.enums.EventhubErrorCode;

public class EventCannotBeModifiedException extends EventhubException{

    public EventCannotBeModifiedException(Long eventId, String status){
        super("Event with ID " + eventId + " cannot be modified because its status is " + status + ".", EventhubErrorCode.EVENT_CANNOT_BE_MODIFIED.getErrorCode(), EventhubErrorCode.EVENT_CANNOT_BE_MODIFIED.getHttpStatusCode());
    }
    
}
