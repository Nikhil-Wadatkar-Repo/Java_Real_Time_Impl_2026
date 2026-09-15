package com.enterprise.payroll.attendance.service;

import com.enterprise.payroll.attendance.dto.AttendanceResponse;
import com.enterprise.payroll.attendance.dto.AttendanceSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.YearMonth;

public interface AttendanceService {

    AttendanceResponse checkIn(Long employeeId);

    AttendanceResponse checkOut(Long employeeId);

    Page<AttendanceResponse> getForEmployee(Long employeeId, YearMonth month, Pageable pageable);

    AttendanceSummaryResponse getSummary(Long employeeId, YearMonth month);
}
