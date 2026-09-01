package com.pointwest.bootcamp.eventhubri.core.exception.communication;

import com.pointwest.bootcamp.eventhubri.core.exception.EventhubException;
import com.pointwest.bootcamp.eventhubri.core.exception.enums.EventhubErrorCode;

public class NotificationForbiddenException extends EventhubException {
    public NotificationForbiddenException(String message) {
        super(EventhubErrorCode.NOTIFICATION_FORBIDDEN, message);
    }
}
