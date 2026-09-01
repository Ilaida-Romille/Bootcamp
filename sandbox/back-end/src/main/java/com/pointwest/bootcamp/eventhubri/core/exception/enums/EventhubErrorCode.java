package com.pointwest.bootcamp.eventhubri.core.exception.enums;

import lombok.Getter;

@Getter
public enum EventhubErrorCode {
    // // Global & Validation
    // VALIDATION_FAILED("VALIDATION_FAILED", 400),
    // MALFORMED_REQUEST_BODY("MALFORMED_REQUEST_BODY", 400),
    // INVALID_PARAMETER_TYPE("INVALID_PARAMETER_TYPE", 400),
    // HTTP_METHOD_NOT_SUPPORTED("HTTP_METHOD_NOT_SUPPORTED", 405),

    // Auth & User Exceptions
    USER_ALREADY_EXISTS("USER_ALREADY_EXISTS", 409),
    INVALID_CREDENTIALS("INVALID_CREDENTIALS", 401),
    USER_INACTIVE("USER_INACTIVE", 403),

    // Refresh Token Exceptions
    INVALID_REFRESH_TOKEN("INVALID_REFRESH_TOKEN", 401),
    REFRESH_TOKEN_REUSE_DETECTED("REFRESH_TOKEN_REUSE_DETECTED", 401),
    REFRESH_TOKEN_EXPIRED("REFRESH_TOKEN_EXPIRED", 401),
    TOKEN_HASHING_FAILED("TOKEN_HASHING_FAILED", 500),
    // Organization Exceptions
    ORGANIZATION_NOT_FOUND("ORGANIZATION_NOT_FOUND", 404),
    ORGANIZATION_ALREADY_EXISTS("ORGANIZATION_ALREADY_EXISTS", 409),
    ORGANIZATION_INACTIVE("ORGANIZATION_INACTIVE", 403),

    // Event Management
    EVENT_NOT_FOUND("EVENT_NOT_FOUND", 404),
    INVALID_EVENT_TIMELINE("INVALID_EVENT_TIMELINE", 422),
    INVALID_EVENT_TYPE_REQUIREMENTS("INVALID_EVENT_TYPE_REQUIREMENTS", 400),

    // // Billing & Invoices
    // INVOICE_NOT_FOUND("INVOICE_NOT_FOUND", 404),
    PAYMENT_OVERDUE("PAYMENT_OVERDUE", 402);

    private final String errorCode;
    private final int httpStatusCode;

    EventhubErrorCode(String errorCode, int httpStatusCode) {
        this.errorCode = errorCode;
        this.httpStatusCode = httpStatusCode;
    }
}
