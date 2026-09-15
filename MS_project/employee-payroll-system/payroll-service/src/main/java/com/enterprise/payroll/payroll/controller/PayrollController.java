package com.enterprise.payroll.payroll.controller;

import com.enterprise.payroll.payroll.dto.PayrollResponse;
import com.enterprise.payroll.payroll.dto.ProcessMonthResult;
import com.enterprise.payroll.payroll.service.PayrollService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;

@RestController
@RequestMapping("/payroll")
public class PayrollController {

    private final PayrollService payrollService;

    public PayrollController(PayrollService payrollService) {
        this.payrollService = payrollService;
    }

    /** POST /payroll/process/{employeeId}?month=2026-09 (defaults to current month). */
    @PostMapping("/process/{employeeId}")
    public ResponseEntity<PayrollResponse> processEmployee(
            @PathVariable Long employeeId,
            @RequestParam(required = false) String month) {
        YearMonth yearMonth = (month != null) ? YearMonth.parse(month) : YearMonth.now();
        return ResponseEntity.status(HttpStatus.CREATED).body(payrollService.processEmployee(employeeId, yearMonth));
    }

    /** POST /payroll/process-month?month=2026-09 - runs payroll for every ACTIVE employee. */
    @PostMapping("/process-month")
    public ResponseEntity<ProcessMonthResult> processMonth(@RequestParam(required = false) String month) {
        YearMonth yearMonth = (month != null) ? YearMonth.parse(month) : YearMonth.now();
        return ResponseEntity.ok(payrollService.processMonth(yearMonth));
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<Page<PayrollResponse>> getForEmployee(@PathVariable Long employeeId, Pageable pageable) {
        return ResponseEntity.ok(payrollService.getForEmployee(employeeId, pageable));
    }

    /** GET /payroll?month=2026-09 */
    @GetMapping
    public ResponseEntity<Page<PayrollResponse>> getForMonth(@RequestParam String month, Pageable pageable) {
        return ResponseEntity.ok(payrollService.getForMonth(YearMonth.parse(month), pageable));
    }
}
