package com.enterprise.payroll.employee.dto;

import com.enterprise.payroll.employee.entity.EmployeeStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/** Outbound representation of an employee. */
public record EmployeeResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        Long departmentId,
        String designation,
        BigDecimal salary,
        EmployeeStatus status,
        LocalDate joiningDate,
        Long version,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
