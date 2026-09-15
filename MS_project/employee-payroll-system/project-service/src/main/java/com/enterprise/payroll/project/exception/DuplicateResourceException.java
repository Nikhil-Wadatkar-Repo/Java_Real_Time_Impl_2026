package com.enterprise.payroll.project.exception;

/** Thrown when a create/update/assignment would violate a uniqueness constraint. Maps to HTTP 409. */
public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}
