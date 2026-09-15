package com.enterprise.payroll.payroll.service;

import com.enterprise.payroll.payroll.dto.PayrollResponse;
import com.enterprise.payroll.payroll.dto.ProcessMonthResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.YearMonth;

public interface PayrollService {

    PayrollResponse processEmployee(Long employeeId, YearMonth month);

    ProcessMonthResult processMonth(YearMonth month);

    Page<PayrollResponse> getForEmployee(Long employeeId, Pageable pageable);

    Page<PayrollResponse> getForMonth(YearMonth month, Pageable pageable);
}
