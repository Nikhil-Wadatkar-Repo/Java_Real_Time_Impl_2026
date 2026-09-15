package com.enterprise.payroll.reporting.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PayrollDto(Long employeeId, BigDecimal netSalary, String status) {
}
