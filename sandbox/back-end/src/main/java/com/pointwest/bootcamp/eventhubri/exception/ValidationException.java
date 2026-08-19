package com.pointwest.bootcamp.eventhubri.exception;

import java.util.Map;

public class ValidationException extends AppException {

    private Map<String, String> errors;

    public ValidationException(String message) {
        super(400, message);
    }

    public ValidationException(String message, Map<String, String> errors) {
        super(400, message);
        this.errors = errors;
    }

    public Map<String, String> errors() {
        return errors;
    }
}