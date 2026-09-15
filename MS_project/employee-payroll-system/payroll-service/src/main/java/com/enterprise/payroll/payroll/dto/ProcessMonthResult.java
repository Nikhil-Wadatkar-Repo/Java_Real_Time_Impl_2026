package com.enterprise.payroll.payroll.dto;

import java.time.YearMonth;
import java.util.List;

/** Summary returned by the batch POST /payroll/process-month endpoint. */
public record ProcessMonthResult(
        YearMonth month,
        int processedCount,
        int failedCount,
        List<Long> failedEmployeeIds
) {
}
