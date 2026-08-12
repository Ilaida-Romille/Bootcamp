package com.hotel.domain;

/**
 * Checked exception modelling a fallible report-generation boundary (file
 * writes, template services). Deliberately checked so callers must handle
 * it, even though this implementation performs no real I/O.
 */
public class InspectionReportException extends Exception {

    public InspectionReportException(String message) {
        super(message);
    }

    public InspectionReportException(String message, Throwable cause) {
        super(message, cause);
    }
}
