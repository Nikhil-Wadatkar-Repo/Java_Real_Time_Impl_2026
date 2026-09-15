package com.enterprise.payroll.employee.entity;

/**
 * Lifecycle state of an employee. Payroll processing only considers
 * ACTIVE and ON_LEAVE employees; TERMINATED employees are excluded but
 * retained for historical/reporting purposes.
 */
public enum EmployeeStatus {
    ACTIVE,
    ON_LEAVE,
    INACTIVE,
    TERMINATED
}
