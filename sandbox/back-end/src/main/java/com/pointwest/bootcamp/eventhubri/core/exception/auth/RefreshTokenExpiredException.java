package com.pointwest.bootcamp.eventhubri.core.exception.auth;

import com.pointwest.bootcamp.eventhubri.core.exception.EventhubException;
import com.pointwest.bootcamp.eventhubri.core.exception.enums.EventhubErrorCode;

public class RefreshTokenExpiredException extends EventhubException {
    public RefreshTokenExpiredException(String message) {
        super(EventhubErrorCode.REFRESH_TOKEN_EXPIRED, message);
    }
}
