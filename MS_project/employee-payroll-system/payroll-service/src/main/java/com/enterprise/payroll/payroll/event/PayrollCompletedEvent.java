package com.enterprise.payroll.payroll.event;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Published to the {@code payroll.completed} topic after a payroll run is
 * persisted. notification-service consumes this to email the employee -
 * deliberately asynchronous (Kafka, not a direct REST call) so payroll
 * processing never blocks on, or fails because of, notification-service.
 */
public record PayrollCompletedEvent(
        Long employeeId,
        LocalDate payMonth,
        BigDecimal netSalary
) {
}
