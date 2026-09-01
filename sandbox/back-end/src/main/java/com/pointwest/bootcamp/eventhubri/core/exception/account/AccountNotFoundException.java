package com.pointwest.bootcamp.eventhubri.core.exception.account;

import com.pointwest.bootcamp.eventhubri.core.exception.EventhubException;
import com.pointwest.bootcamp.eventhubri.core.exception.enums.EventhubErrorCode;

public class AccountNotFoundException extends EventhubException {
    public AccountNotFoundException(String message) {
        super(EventhubErrorCode.USER_NOT_FOUND, message);
    }
}
