package com.enterprise.payroll.employee.dto;

import com.enterprise.payroll.employee.entity.EmployeeStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Optional filters for GET /employees/search. Every field is nullable;
 * {@link com.enterprise.payroll.employee.repository.spec.EmployeeSpecifications}
 * only applies a predicate for the ones actually supplied.
 */
public record EmployeeSearchCriteria(
        String name,
        Long departmentId,
        EmployeeStatus status,
        BigDecimal minSalary,
        BigDecimal maxSalary,
        LocalDate joinedAfter,
        LocalDate joinedBefore
) {
}
