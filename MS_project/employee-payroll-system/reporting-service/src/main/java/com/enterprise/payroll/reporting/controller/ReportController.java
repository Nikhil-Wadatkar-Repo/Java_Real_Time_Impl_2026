package com.enterprise.payroll.reporting.controller;

import com.enterprise.payroll.reporting.dto.ReportResponse;
import com.enterprise.payroll.reporting.service.ReportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.YearMonth;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * POST /reports/payroll?month=2026-09 - returns 202 immediately; the
     * report is generated on a background thread pool. Poll GET
     * /reports/{id} for the result.
     */
    @PostMapping("/payroll")
    public ResponseEntity<Void> generatePayrollReport(@RequestParam(required = false) String month) {
        YearMonth yearMonth = (month != null) ? YearMonth.parse(month) : YearMonth.now();
        Long reportId = reportService.triggerPayrollReport(yearMonth.toString());
        return ResponseEntity.accepted().location(URI.create("/reports/" + reportId)).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(reportService.getById(id));
    }
}
