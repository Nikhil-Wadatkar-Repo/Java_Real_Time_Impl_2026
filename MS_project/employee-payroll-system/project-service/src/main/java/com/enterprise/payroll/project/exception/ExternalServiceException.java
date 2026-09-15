package com.enterprise.payroll.project.exception;

/**
 * Thrown when a downstream service call fails for reasons outside the
 * caller's control (connection refused, timeout, 5xx from the callee).
 * Maps to HTTP 503. See employee-service's equivalent class for the full
 * rationale on why this is distinct from {@link InvalidRequestException}.
 */
public class ExternalServiceException extends RuntimeException {
    public ExternalServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
