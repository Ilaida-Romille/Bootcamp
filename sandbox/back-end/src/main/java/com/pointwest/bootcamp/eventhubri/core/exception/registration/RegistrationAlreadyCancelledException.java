package com.pointwest.bootcamp.eventhubri.core.exception.registration;

import com.pointwest.bootcamp.eventhubri.core.exception.EventhubException;
import com.pointwest.bootcamp.eventhubri.core.exception.enums.EventhubErrorCode;

public class RegistrationAlreadyCancelledException extends EventhubException {
    public RegistrationAlreadyCancelledException(Long registrationId) {
        super(EventhubErrorCode.REGISTRATION_ALREADY_CANCELLED, "Registration is already cancelled: " + registrationId);
    }
}
