package com.enterprise.payroll.payroll.mapper;

import com.enterprise.payroll.payroll.dto.PayrollResponse;
import com.enterprise.payroll.payroll.entity.Payroll;
import org.springframework.stereotype.Component;

@Component
public class PayrollMapper {

    public PayrollResponse toResponse(Payroll payroll) {
        return new PayrollResponse(
                payroll.getId(),
                payroll.getEmployeeId(),
                payroll.getPayMonth(),
                payroll.getBasicSalary(),
                payroll.getDeductions(),
                payroll.getNetSalary(),
                payroll.getPresentDays(),
                payroll.getAbsentDays(),
                payroll.getStatus(),
                payroll.getProcessedAt()
        );
    }
}
