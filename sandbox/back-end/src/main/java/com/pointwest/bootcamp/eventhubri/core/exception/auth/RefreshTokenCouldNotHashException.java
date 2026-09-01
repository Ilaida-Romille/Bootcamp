package com.pointwest.bootcamp.eventhubri.core.exception.auth;

import com.pointwest.bootcamp.eventhubri.core.exception.EventhubException;
import com.pointwest.bootcamp.eventhubri.core.exception.enums.EventhubErrorCode;

public class RefreshTokenCouldNotHashException extends EventhubException {

    public RefreshTokenCouldNotHashException(String message, Throwable cause) {
        super(EventhubErrorCode.TOKEN_HASHING_FAILED, message, cause);
    }

}
