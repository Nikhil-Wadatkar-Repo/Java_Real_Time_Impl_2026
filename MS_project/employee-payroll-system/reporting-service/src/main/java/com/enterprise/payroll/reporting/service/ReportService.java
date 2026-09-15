package com.enterprise.payroll.reporting.service;

import com.enterprise.payroll.reporting.dto.ReportResponse;

public interface ReportService {

    /** Inserts a PENDING report row synchronously and returns its id immediately. */
    Long triggerPayrollReport(String month);

    ReportResponse getById(Long id);
}
