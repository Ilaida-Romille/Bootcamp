package com.pointwest.bootcamp.eventhubri.core.exception.auth;

import com.pointwest.bootcamp.eventhubri.core.exception.EventhubException;
import com.pointwest.bootcamp.eventhubri.core.exception.enums.EventhubErrorCode;

public class OrganizationNotFounException extends EventhubException {
    public OrganizationNotFounException(String message) {
        super(EventhubErrorCode.ORGANIZATION_NOT_FOUND, message);
    }
}
