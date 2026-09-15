package com.enterprise.payroll.payroll.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EmployeeDto(
        Long id,
        String firstName,
        String lastName,
        BigDecimal salary,
        String status
) {
}
