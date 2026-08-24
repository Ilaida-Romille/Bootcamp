package com.pointwest.bootcamp.eventhubri.exception;

public class NotFoundException extends AppException {
    public NotFoundException() {
        super(404, "Item Not Found");
    }
}
