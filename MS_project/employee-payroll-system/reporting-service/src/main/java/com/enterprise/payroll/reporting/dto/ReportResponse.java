package com.enterprise.payroll.reporting.dto;

import com.enterprise.payroll.reporting.entity.ReportStatus;
import com.enterprise.payroll.reporting.entity.ReportType;

import java.time.LocalDateTime;

public record ReportResponse(
        Long id,
        ReportType type,
        String parameter,
        ReportStatus status,
        String resultSummary,
        String errorMessage,
        LocalDateTime createdAt,
        LocalDateTime completedAt
) {
}
