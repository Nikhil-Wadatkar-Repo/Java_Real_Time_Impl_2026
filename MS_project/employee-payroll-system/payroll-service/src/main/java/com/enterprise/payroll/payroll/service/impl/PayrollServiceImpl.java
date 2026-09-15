package com.enterprise.payroll.payroll.service.impl;

import com.enterprise.payroll.payroll.client.AttendanceClient;
import com.enterprise.payroll.payroll.client.EmployeeClient;
import com.enterprise.payroll.payroll.client.dto.AttendanceSummaryDto;
import com.enterprise.payroll.payroll.client.dto.EmployeeDto;
import com.enterprise.payroll.payroll.dto.PayrollResponse;
import com.enterprise.payroll.payroll.dto.ProcessMonthResult;
import com.enterprise.payroll.payroll.entity.Payroll;
import com.enterprise.payroll.payroll.entity.PayrollStatus;
import com.enterprise.payroll.payroll.event.PayrollCompletedEvent;
import com.enterprise.payroll.payroll.event.PayrollEventPublisher;
import com.enterprise.payroll.payroll.exception.DuplicateResourceException;
import com.enterprise.payroll.payroll.exception.ExternalServiceException;
import com.enterprise.payroll.payroll.exception.InvalidRequestException;
import com.enterprise.payroll.payroll.mapper.PayrollMapper;
import com.enterprise.payroll.payroll.repository.PayrollRepository;
import com.enterprise.payroll.payroll.service.PayrollService;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PayrollServiceImpl implements PayrollService {

    private static final Logger log = LoggerFactory.getLogger(PayrollServiceImpl.class);
    private static final BigDecimal DAYS_PER_MONTH = BigDecimal.valueOf(30);

    private final PayrollRepository payrollRepository;
    private final PayrollMapper payrollMapper;
    private final EmployeeClient employeeClient;
    private final AttendanceClient attendanceClient;
    private final PayrollEventPublisher payrollEventPublisher;

    public PayrollServiceImpl(PayrollRepository payrollRepository, PayrollMapper payrollMapper,
                               EmployeeClient employeeClient, AttendanceClient attendanceClient,
                               PayrollEventPublisher payrollEventPublisher) {
        this.payrollRepository = payrollRepository;
        this.payrollMapper = payrollMapper;
        this.employeeClient = employeeClient;
        this.attendanceClient = attendanceClient;
        this.payrollEventPublisher = payrollEventPublisher;
    }

    @Override
    public PayrollResponse processEmployee(Long employeeId, YearMonth month) {
        var payMonth = month.atDay(1);
        if (payrollRepository.existsByEmployeeIdAndPayMonth(employeeId, payMonth)) {
            throw new DuplicateResourceException(
                    "Payroll for employee " + employeeId + " has already been processed for " + month);
        }

        EmployeeDto employee = fetchEmployee(employeeId);
        AttendanceSummaryDto summary = fetchAttendanceSummary(employeeId, month);

        BigDecimal dailyRate = employee.salary().divide(DAYS_PER_MONTH, 2, RoundingMode.HALF_UP);
        BigDecimal deductions = dailyRate.multiply(BigDecimal.valueOf(summary.absentDays()));
        BigDecimal netSalary = employee.salary().subtract(deductions);

        Payroll payroll = Payroll.builder()
                .employeeId(employeeId)
                .payMonth(payMonth)
                .basicSalary(employee.salary())
                .deductions(deductions)
                .netSalary(netSalary)
                .presentDays((int) summary.presentDays())
                .absentDays((int) summary.absentDays())
                .status(PayrollStatus.PROCESSED)
                .processedAt(LocalDateTime.now())
                .build();

        Payroll saved = payrollRepository.save(payroll);
        log.info("Processed payroll id={} employeeId={} month={} netSalary={}",
                saved.getId(), employeeId, month, netSalary);

        payrollEventPublisher.publishPayrollCompleted(
                new PayrollCompletedEvent(employeeId, payMonth, netSalary));

        return payrollMapper.toResponse(saved);
    }

    @Override
    public ProcessMonthResult processMonth(YearMonth month) {
        List<EmployeeDto> activeEmployees = fetchActiveEmployees();
        int processed = 0;
        List<Long> failed = new ArrayList<>();

        for (EmployeeDto employee : activeEmployees) {
            try {
                processEmployee(employee.id(), month);
                processed++;
            } catch (DuplicateResourceException ex) {
                // Already processed this month - not a failure, just a no-op.
                log.info("Skipping employeeId={} - already processed for {}", employee.id(), month);
            } catch (Exception ex) {
                log.error("Failed to process payroll for employeeId={} month={}", employee.id(), month, ex);
                failed.add(employee.id());
            }
        }

        log.info("Batch payroll run for {} complete: {} processed, {} failed", month, processed, failed.size());
        return new ProcessMonthResult(month, processed, failed.size(), failed);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PayrollResponse> getForEmployee(Long employeeId, Pageable pageable) {
        return payrollRepository.findByEmployeeId(employeeId, pageable).map(payrollMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PayrollResponse> getForMonth(YearMonth month, Pageable pageable) {
        return payrollRepository.findByPayMonth(month.atDay(1), pageable).map(payrollMapper::toResponse);
    }

    private EmployeeDto fetchEmployee(Long employeeId) {
        try {
            return employeeClient.getEmployeeById(employeeId);
        } catch (FeignException.NotFound ex) {
            throw new InvalidRequestException("Employee not found with id: " + employeeId);
        } catch (FeignException ex) {
            log.error("employee-service call failed for employeeId={}", employeeId, ex);
            throw new ExternalServiceException("employee-service is currently unavailable", ex);
        }
    }

    private AttendanceSummaryDto fetchAttendanceSummary(Long employeeId, YearMonth month) {
        try {
            return attendanceClient.getSummary(employeeId, month.toString());
        } catch (FeignException ex) {
            log.error("attendance-service call failed for employeeId={} month={}", employeeId, month, ex);
            throw new ExternalServiceException("attendance-service is currently unavailable", ex);
        }
    }

    private List<EmployeeDto> fetchActiveEmployees() {
        try {
            return employeeClient.searchEmployees("ACTIVE", 1000).content();
        } catch (FeignException ex) {
            log.error("employee-service call failed while listing active employees", ex);
            throw new ExternalServiceException("employee-service is currently unavailable", ex);
        }
    }
}
