package com.enterprise.payroll.attendance.repository;

import com.enterprise.payroll.attendance.entity.Attendance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    Optional<Attendance> findByEmployeeIdAndWorkDate(Long employeeId, LocalDate workDate);

    Page<Attendance> findByEmployeeId(Long employeeId, Pageable pageable);

    Page<Attendance> findByEmployeeIdAndWorkDateBetween(Long employeeId, LocalDate from, LocalDate to, Pageable pageable);

    List<Attendance> findByEmployeeIdAndWorkDateBetween(Long employeeId, LocalDate from, LocalDate to);
}
