package com.pointwest.bootcamp.eventhubri.core.exception;

import com.pointwest.bootcamp.eventhubri.core.exception.enums.EventhubErrorCode;

public class BusinessRuleViolationException extends EventhubException {
    public BusinessRuleViolationException(String message) {
        super(EventhubErrorCode.BUSINESS_RULE_VIOLATION, message);
    }
}
