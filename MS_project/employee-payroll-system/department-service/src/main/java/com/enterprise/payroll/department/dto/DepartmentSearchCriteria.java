package com.enterprise.payroll.department.dto;

/**
 * Optional filters for GET /departments/search. Every field is nullable;
 * only supplied ones become a predicate.
 */
public record DepartmentSearchCriteria(
        String name,
        String code,
        String location
) {
}
