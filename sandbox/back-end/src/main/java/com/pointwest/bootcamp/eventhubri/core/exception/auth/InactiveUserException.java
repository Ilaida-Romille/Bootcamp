package com.pointwest.bootcamp.eventhubri.core.exception.auth;

import com.pointwest.bootcamp.eventhubri.core.exception.EventhubException;
import com.pointwest.bootcamp.eventhubri.core.exception.enums.EventhubErrorCode;

public class InactiveUserException extends EventhubException {

    public InactiveUserException(String message) {
        super(EventhubErrorCode.USER_INACTIVE, message);
    }
}
