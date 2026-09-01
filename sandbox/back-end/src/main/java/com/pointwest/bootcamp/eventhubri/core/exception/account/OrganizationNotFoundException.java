package com.pointwest.bootcamp.eventhubri.core.exception.account;

import com.pointwest.bootcamp.eventhubri.core.exception.EventhubException;
import com.pointwest.bootcamp.eventhubri.core.exception.enums.EventhubErrorCode;

public class OrganizationNotFoundException extends EventhubException {
    public OrganizationNotFoundException(Long organizationId) {
        super(EventhubErrorCode.ORGANIZATION_NOT_FOUND, "Organization not found: " + organizationId);
    }
}
