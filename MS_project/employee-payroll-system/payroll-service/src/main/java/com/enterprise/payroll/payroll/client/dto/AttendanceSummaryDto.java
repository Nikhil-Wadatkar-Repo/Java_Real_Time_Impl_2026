package com.enterprise.payroll.payroll.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AttendanceSummaryDto(
        Long employeeId,
        String month,
        long presentDays,
        long halfDays,
        long onLeaveDays,
        long absentDays,
        BigDecimal totalWorkingHours
) {
}
