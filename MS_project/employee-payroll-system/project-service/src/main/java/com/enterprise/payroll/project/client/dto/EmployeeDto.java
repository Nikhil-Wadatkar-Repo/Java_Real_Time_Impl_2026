package com.enterprise.payroll.project.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Project-service's own, minimal view of an employee, as returned by
 * employee-service's API. Deliberately not the same class as
 * employee-service's own {@code EmployeeResponse} - this client only needs
 * enough fields to validate a reference.
 * {@code @JsonIgnoreProperties(ignoreUnknown = true)} because
 * employee-service's actual payload has many more fields (salary, status,
 * joiningDate, version, timestamps) this consumer doesn't care about.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record EmployeeDto(
        Long id,
        String firstName,
        String lastName
) {
}
