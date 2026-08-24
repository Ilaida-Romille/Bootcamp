package com.pointwest.bootcamp.eventhubri.exception;

public class AppException extends Exception {

    private int statusCode;

    public AppException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public AppException(int statusCode, String message, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public AppException(String message) {
        super(message);
        this.statusCode = 500;
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = 500;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
