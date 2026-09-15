package com.enterprise.payroll.attendance.dto;

import java.math.BigDecimal;
import java.time.YearMonth;

/** Monthly rollup consumed by Payroll Service to compute salary. */
public record AttendanceSummaryResponse(
        Long employeeId,
        YearMonth month,
        long presentDays,
        long halfDays,
        long onLeaveDays,
        long absentDays,
        BigDecimal totalWorkingHours
) {
}
