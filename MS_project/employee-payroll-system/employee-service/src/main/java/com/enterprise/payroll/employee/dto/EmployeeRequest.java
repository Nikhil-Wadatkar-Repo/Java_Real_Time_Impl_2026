package com.enterprise.payroll.employee.dto;

import com.enterprise.payroll.employee.entity.EmployeeStatus;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Inbound payload for creating/updating an employee. Immutable by design -
 * once validated, nothing downstream should be able to mutate the request.
 */
public record EmployeeRequest(

        @NotBlank(message = "First name is required")
        @Size(max = 100, message = "First name must not exceed 100 characters")
        String firstName,

        @NotBlank(message = "Last name is required")
        @Size(max = 100, message = "Last name must not exceed 100 characters")
        String lastName,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be a valid address")
        String email,

        @NotNull(message = "Department id is required")
        @Positive(message = "Department id must be positive")
        Long departmentId,

        @Size(max = 100, message = "Designation must not exceed 100 characters")
        String designation,

        @NotNull(message = "Salary is required")
        @Positive(message = "Salary must be positive")
        BigDecimal salary,

        @NotNull(message = "Status is required")
        EmployeeStatus status,

        @NotNull(message = "Joining date is required")
        @PastOrPresent(message = "Joining date cannot be in the future")
        LocalDate joiningDate
) {
}
