package com.pointwest.bootcamp.eventhubri.core.exception;

public abstract class EventhubException extends RuntimeException{
    private final String errorCode;
    private final int httpStatusCode;

    protected EventhubException(String errorMessage, Throwable cause, String errorCode, int httpStatusCode){
        super(errorMessage, cause);
        this.errorCode = errorCode;
        this.httpStatusCode = httpStatusCode;
    }

    protected EventhubException(String errorMessage, String errorCode, int httpStatusCode){
        super(errorMessage);
        this.errorCode = errorCode;
        this.httpStatusCode = httpStatusCode;
    }

    public String getErrorCode(){
        return errorCode;
    }

    public int getHttpStatusCode(){
        return httpStatusCode;
    }

}
