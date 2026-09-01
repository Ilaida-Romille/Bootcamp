package com.pointwest.bootcamp.eventhubri.core.exception.account;

import com.pointwest.bootcamp.eventhubri.core.exception.EventhubException;
import com.pointwest.bootcamp.eventhubri.core.exception.enums.EventhubErrorCode;

public class OrganizationAlreadyExistException extends EventhubException {
    public OrganizationAlreadyExistException(String message) {
        super(EventhubErrorCode.ORGANIZATION_ALREADY_EXISTS, message);
    }
}
