package com.enterprise.payroll.project.exception;

/**
 * Thrown when the request itself is well-formed but references something
 * invalid in context - e.g. an {@code employeeId} that doesn't exist, or a
 * project {@code endDate} before its {@code startDate}. Maps to HTTP 400.
 */
public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(String message) {
        super(message);
    }
}
