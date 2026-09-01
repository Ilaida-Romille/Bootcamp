package com.pointwest.bootcamp.eventhubri.core.exception.auth;

import com.pointwest.bootcamp.eventhubri.core.exception.EventhubException;
import com.pointwest.bootcamp.eventhubri.core.exception.enums.EventhubErrorCode;

public class RefreshTokenReusedException extends EventhubException {
    public RefreshTokenReusedException(String message) {
        super(EventhubErrorCode.REFRESH_TOKEN_REUSE_DETECTED, message);
    }

}
