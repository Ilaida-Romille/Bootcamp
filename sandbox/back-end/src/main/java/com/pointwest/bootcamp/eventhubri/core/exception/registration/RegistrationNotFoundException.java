package com.pointwest.bootcamp.eventhubri.core.exception.registration;

import com.pointwest.bootcamp.eventhubri.core.exception.EventhubException;
import com.pointwest.bootcamp.eventhubri.core.exception.enums.EventhubErrorCode;

public class RegistrationNotFoundException extends EventhubException {
    public RegistrationNotFoundException(Long registrationId) {
        super(EventhubErrorCode.REGISTRATION_NOT_FOUND, "Registration not found: " + registrationId);
    }
}
