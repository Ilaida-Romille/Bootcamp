package com.pointwest.bootcamp.eventhubri.core.exception.event;

import com.pointwest.bootcamp.eventhubri.core.exception.EventhubException;
import com.pointwest.bootcamp.eventhubri.core.exception.enums.EventhubErrorCode;

public class InvalidEventTimelineException extends EventhubException {
    public InvalidEventTimelineException(String message) {
        super(EventhubErrorCode.INVALID_EVENT_TIMELINE, message);
    }
}
