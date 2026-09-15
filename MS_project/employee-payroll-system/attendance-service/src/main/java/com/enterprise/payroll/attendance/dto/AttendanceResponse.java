package com.enterprise.payroll.attendance.dto;

import com.enterprise.payroll.attendance.entity.AttendanceStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record AttendanceResponse(
        Long id,
        Long employeeId,
        LocalDate workDate,
        LocalDateTime checkInTime,
        LocalDateTime checkOutTime,
        BigDecimal workingHours,
        AttendanceStatus status
) {
}
