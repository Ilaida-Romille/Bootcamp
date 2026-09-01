package com.pointwest.bootcamp.eventhubri.core.exception.registration;

import com.pointwest.bootcamp.eventhubri.core.exception.EventhubException;
import com.pointwest.bootcamp.eventhubri.core.exception.enums.EventhubErrorCode;

public class RegistrationPeriodClosedException extends EventhubException {
    public RegistrationPeriodClosedException(Long eventId) {
        super(EventhubErrorCode.REGISTRATION_PERIOD_CLOSED, "Registration period has ended for event: " + eventId);
    }
}
