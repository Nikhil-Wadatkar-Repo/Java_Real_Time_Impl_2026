package com.enterprise.payroll.attendance.service.impl;

import com.enterprise.payroll.attendance.client.EmployeeClient;
import com.enterprise.payroll.attendance.dto.AttendanceResponse;
import com.enterprise.payroll.attendance.dto.AttendanceSummaryResponse;
import com.enterprise.payroll.attendance.entity.Attendance;
import com.enterprise.payroll.attendance.entity.AttendanceStatus;
import com.enterprise.payroll.attendance.exception.DuplicateResourceException;
import com.enterprise.payroll.attendance.exception.ExternalServiceException;
import com.enterprise.payroll.attendance.exception.InvalidRequestException;
import com.enterprise.payroll.attendance.mapper.AttendanceMapper;
import com.enterprise.payroll.attendance.repository.AttendanceRepository;
import com.enterprise.payroll.attendance.service.AttendanceService;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Service
@Transactional
public class AttendanceServiceImpl implements AttendanceService {

    private static final Logger log = LoggerFactory.getLogger(AttendanceServiceImpl.class);

    private final AttendanceRepository attendanceRepository;
    private final AttendanceMapper attendanceMapper;
    private final EmployeeClient employeeClient;

    public AttendanceServiceImpl(AttendanceRepository attendanceRepository, AttendanceMapper attendanceMapper,
                                  EmployeeClient employeeClient) {
        this.attendanceRepository = attendanceRepository;
        this.attendanceMapper = attendanceMapper;
        this.employeeClient = employeeClient;
    }

    @Override
    public AttendanceResponse checkIn(Long employeeId) {
        validateEmployeeExists(employeeId);
        LocalDate today = LocalDate.now();
        if (attendanceRepository.findByEmployeeIdAndWorkDate(employeeId, today).isPresent()) {
            throw new DuplicateResourceException(
                    "Employee " + employeeId + " has already checked in today (" + today + ")");
        }
        Attendance attendance = Attendance.builder()
                .employeeId(employeeId)
                .workDate(today)
                .checkInTime(LocalDateTime.now())
                .status(AttendanceStatus.PRESENT)
                .build();
        Attendance saved = attendanceRepository.save(attendance);
        log.info("Employee id={} checked in at {}", employeeId, saved.getCheckInTime());
        return attendanceMapper.toResponse(saved);
    }

    @Override
    public AttendanceResponse checkOut(Long employeeId) {
        LocalDate today = LocalDate.now();
        Attendance attendance = attendanceRepository.findByEmployeeIdAndWorkDate(employeeId, today)
                .orElseThrow(() -> new InvalidRequestException(
                        "Employee " + employeeId + " has not checked in today (" + today + ")"));
        if (attendance.getCheckOutTime() != null) {
            throw new InvalidRequestException("Employee " + employeeId + " has already checked out today");
        }
        LocalDateTime checkOutTime = LocalDateTime.now();
        attendance.setCheckOutTime(checkOutTime);
        BigDecimal hours = BigDecimal.valueOf(
                        Duration.between(attendance.getCheckInTime(), checkOutTime).toMinutes())
                .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
        attendance.setWorkingHours(hours);
        attendance.setStatus(hours.compareTo(BigDecimal.valueOf(4)) < 0 ? AttendanceStatus.HALF_DAY : AttendanceStatus.PRESENT);
        Attendance saved = attendanceRepository.save(attendance);
        log.info("Employee id={} checked out at {} ({} hours)", employeeId, checkOutTime, hours);
        return attendanceMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AttendanceResponse> getForEmployee(Long employeeId, YearMonth month, Pageable pageable) {
        Page<Attendance> page = (month != null)
                ? attendanceRepository.findByEmployeeIdAndWorkDateBetween(
                        employeeId, month.atDay(1), month.atEndOfMonth(), pageable)
                : attendanceRepository.findByEmployeeId(employeeId, pageable);
        return page.map(attendanceMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public AttendanceSummaryResponse getSummary(Long employeeId, YearMonth month) {
        List<Attendance> records = attendanceRepository.findByEmployeeIdAndWorkDateBetween(
                employeeId, month.atDay(1), month.atEndOfMonth());

        long present = records.stream().filter(a -> a.getStatus() == AttendanceStatus.PRESENT).count();
        long halfDays = records.stream().filter(a -> a.getStatus() == AttendanceStatus.HALF_DAY).count();
        long onLeave = records.stream().filter(a -> a.getStatus() == AttendanceStatus.ON_LEAVE).count();
        long absent = records.stream().filter(a -> a.getStatus() == AttendanceStatus.ABSENT).count();
        BigDecimal totalHours = records.stream()
                .map(Attendance::getWorkingHours)
                .filter(java.util.Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new AttendanceSummaryResponse(employeeId, month, present, halfDays, onLeave, absent, totalHours);
    }

    private void validateEmployeeExists(Long employeeId) {
        try {
            employeeClient.getEmployeeById(employeeId);
        } catch (FeignException.NotFound ex) {
            throw new InvalidRequestException("Employee not found with id: " + employeeId);
        } catch (FeignException ex) {
            log.error("employee-service call failed for employeeId={}", employeeId, ex);
            throw new ExternalServiceException("employee-service is currently unavailable", ex);
        }
    }
}
