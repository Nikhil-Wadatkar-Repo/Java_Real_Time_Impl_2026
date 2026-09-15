package com.enterprise.payroll.employee.exception;

/**
 * Thrown when a downstream service call fails for reasons outside the
 * caller's control (connection refused, timeout, 5xx from the callee).
 * Maps to HTTP 503 - the request itself was valid, but the system
 * couldn't fulfil it right now. Distinct from {@link InvalidRequestException}
 * (caller's fault, 400) on purpose, since the client-side handling should
 * differ: retry later vs. fix the request.
 */
public class ExternalServiceException extends RuntimeException {
    public ExternalServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
