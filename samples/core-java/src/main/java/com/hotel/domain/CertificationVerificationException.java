package com.hotel.domain;

/**
 * Checked exception modelling a fallible external boundary (a certification
 * registry lookup). Deliberately checked so callers must handle it, even
 * though this implementation performs no real I/O.
 */
public class CertificationVerificationException extends Exception {

    public CertificationVerificationException(String message) {
        super(message);
    }

    public CertificationVerificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
