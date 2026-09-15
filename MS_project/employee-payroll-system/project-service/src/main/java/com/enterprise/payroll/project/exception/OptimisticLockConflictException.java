package com.enterprise.payroll.project.exception;

/**
 * Thrown when an update was based on a stale version. Maps to HTTP 409 -
 * the client should re-fetch the resource and retry.
 */
public class OptimisticLockConflictException extends RuntimeException {
    public OptimisticLockConflictException(String message) {
        super(message);
    }
}
