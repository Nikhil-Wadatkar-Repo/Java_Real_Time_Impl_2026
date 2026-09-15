package com.enterprise.payroll.attendance.controller;

import com.enterprise.payroll.attendance.dto.AttendanceResponse;
import com.enterprise.payroll.attendance.dto.AttendanceSummaryResponse;
import com.enterprise.payroll.attendance.dto.EmployeeIdRequest;
import com.enterprise.payroll.attendance.service.AttendanceService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping("/check-in")
    public ResponseEntity<AttendanceResponse> checkIn(@Valid @RequestBody EmployeeIdRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(attendanceService.checkIn(request.employeeId()));
    }

    @PostMapping("/check-out")
    public ResponseEntity<AttendanceResponse> checkOut(@Valid @RequestBody EmployeeIdRequest request) {
        return ResponseEntity.ok(attendanceService.checkOut(request.employeeId()));
    }

    /** GET /attendance/{employeeId}?month=2026-09 - month optional, returns all records when omitted. */
    @GetMapping("/{employeeId}")
    public ResponseEntity<Page<AttendanceResponse>> getForEmployee(
            @PathVariable Long employeeId,
            @RequestParam(required = false) String month,
            Pageable pageable) {
        YearMonth yearMonth = (month != null) ? YearMonth.parse(month) : null;
        return ResponseEntity.ok(attendanceService.getForEmployee(employeeId, yearMonth, pageable));
    }

    /** GET /attendance/{employeeId}/summary?month=2026-09 - consumed by Payroll Service. */
    @GetMapping("/{employeeId}/summary")
    public ResponseEntity<AttendanceSummaryResponse> getSummary(
            @PathVariable Long employeeId,
            @RequestParam String month) {
        return ResponseEntity.ok(attendanceService.getSummary(employeeId, YearMonth.parse(month)));
    }
}
