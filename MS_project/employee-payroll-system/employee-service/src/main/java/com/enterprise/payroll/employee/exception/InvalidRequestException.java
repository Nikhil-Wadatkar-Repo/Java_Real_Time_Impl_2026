package com.enterprise.payroll.employee.exception;

/**
 * Thrown when the request itself is well-formed (passes bean validation)
 * but references something invalid in context - e.g. a {@code departmentId}
 * that doesn't exist. Maps to HTTP 400.
 */
public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(String message) {
        super(message);
    }
}
