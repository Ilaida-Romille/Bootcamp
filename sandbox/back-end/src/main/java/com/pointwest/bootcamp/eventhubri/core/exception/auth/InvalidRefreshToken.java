package com.pointwest.bootcamp.eventhubri.core.exception.auth;

import com.pointwest.bootcamp.eventhubri.core.exception.EventhubException;
import com.pointwest.bootcamp.eventhubri.core.exception.enums.EventhubErrorCode;

public class InvalidRefreshToken extends EventhubException {
    public InvalidRefreshToken(String message) {
        super(EventhubErrorCode.INVALID_REFRESH_TOKEN, message);
    }
}
