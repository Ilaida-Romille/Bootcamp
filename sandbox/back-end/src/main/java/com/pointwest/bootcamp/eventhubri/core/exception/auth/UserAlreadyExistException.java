package com.pointwest.bootcamp.eventhubri.core.exception.auth;

import com.pointwest.bootcamp.eventhubri.core.exception.EventhubException;
import com.pointwest.bootcamp.eventhubri.core.exception.enums.EventhubErrorCode;

public class UserAlreadyExistException extends EventhubException {

    public UserAlreadyExistException(String message) {
        super(EventhubErrorCode.USER_ALREADY_EXISTS, message);
    }
}
