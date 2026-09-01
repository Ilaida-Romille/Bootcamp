package com.pointwest.bootcamp.eventhubri.core.exception.auth;

import com.pointwest.bootcamp.eventhubri.core.exception.EventhubException;
import com.pointwest.bootcamp.eventhubri.core.exception.enums.EventhubErrorCode;

public class InvalidEmailOrPasswordException extends EventhubException {

    public InvalidEmailOrPasswordException(String message) {
        super(EventhubErrorCode.INVALID_CREDENTIALS, message);
    }

}
