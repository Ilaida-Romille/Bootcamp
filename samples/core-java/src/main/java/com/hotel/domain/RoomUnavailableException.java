package com.hotel.domain;

/** Thrown when an operation requires a room that is not currently available. */
public class RoomUnavailableException extends HotelException {

    public RoomUnavailableException(String message) {
        super(message);
    }

    public RoomUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
