package com.enterprise.payroll.notification.event;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Mirrors payroll-service's event of the same name. Each service owns its
 * own copy of the event contract on purpose - no shared "events" library
 * module, for the same reason error-response DTOs aren't shared (see
 * docs/DECISIONS.md): avoids compile-time coupling between independently
 * deployable services.
 */
public record PayrollCompletedEvent(
        Long employeeId,
        LocalDate payMonth,
        BigDecimal netSalary
) {
}
