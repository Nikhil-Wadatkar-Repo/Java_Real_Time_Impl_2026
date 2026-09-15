package com.enterprise.payroll.department.exception;

/** Thrown when a create/update would violate a uniqueness constraint. Maps to HTTP 409. */
public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}
