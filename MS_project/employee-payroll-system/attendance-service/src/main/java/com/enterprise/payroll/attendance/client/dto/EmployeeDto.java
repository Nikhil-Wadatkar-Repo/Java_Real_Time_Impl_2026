package com.enterprise.payroll.attendance.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EmployeeDto(Long id, String firstName, String lastName) {
}
