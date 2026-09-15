package com.enterprise.payroll.project.dto;

import java.time.LocalDateTime;

/** Outbound representation of an employee's assignment to a project. */
public record AssignmentResponse(
        Long projectId,
        Long employeeId,
        LocalDateTime assignedAt
) {
}
