package com.pointwest.bootcamp.eventhubri.core.exception.account;

import com.pointwest.bootcamp.eventhubri.core.exception.EventhubException;
import com.pointwest.bootcamp.eventhubri.core.exception.enums.EventhubErrorCode;

public class UserInactiveException extends EventhubException {
    public UserInactiveException(String message) {
        super(EventhubErrorCode.USER_INACTIVE, message);
    }
}
