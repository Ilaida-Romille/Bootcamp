package com.pointwest.bootcamp.eventhubri.core.exception.communication;

import com.pointwest.bootcamp.eventhubri.core.exception.EventhubException;
import com.pointwest.bootcamp.eventhubri.core.exception.enums.EventhubErrorCode;

public class NotificationRecipientNotRegisteredException extends EventhubException {
    public NotificationRecipientNotRegisteredException(Long eventId) {
        super(EventhubErrorCode.RECIPIENT_NOT_REGISTERED, "Recipient is not registered for event: " + eventId);
    }
}
