package com.enterprise.payroll.payroll.dto;

import com.enterprise.payroll.payroll.entity.PayrollStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record PayrollResponse(
        Long id,
        Long employeeId,
        LocalDate payMonth,
        BigDecimal basicSalary,
        BigDecimal deductions,
        BigDecimal netSalary,
        Integer presentDays,
        Integer absentDays,
        PayrollStatus status,
        LocalDateTime processedAt
) {
}
