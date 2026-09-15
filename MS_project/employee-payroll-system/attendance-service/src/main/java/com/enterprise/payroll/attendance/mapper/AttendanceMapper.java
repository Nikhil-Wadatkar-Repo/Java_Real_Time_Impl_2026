package com.enterprise.payroll.attendance.mapper;

import com.enterprise.payroll.attendance.dto.AttendanceResponse;
import com.enterprise.payroll.attendance.entity.Attendance;
import org.springframework.stereotype.Component;

@Component
public class AttendanceMapper {

    public AttendanceResponse toResponse(Attendance attendance) {
        return new AttendanceResponse(
                attendance.getId(),
                attendance.getEmployeeId(),
                attendance.getWorkDate(),
                attendance.getCheckInTime(),
                attendance.getCheckOutTime(),
                attendance.getWorkingHours(),
                attendance.getStatus()
        );
    }
}
