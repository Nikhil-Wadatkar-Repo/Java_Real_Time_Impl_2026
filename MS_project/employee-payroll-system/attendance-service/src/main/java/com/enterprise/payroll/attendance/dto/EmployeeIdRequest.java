package com.enterprise.payroll.attendance.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/** Shared body shape for check-in/check-out requests. */
public record EmployeeIdRequest(
        @NotNull(message = "Employee id is required")
        @Positive(message = "Employee id must be positive")
        Long employeeId
) {
}
