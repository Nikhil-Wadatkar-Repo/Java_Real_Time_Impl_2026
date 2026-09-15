package com.enterprise.payroll.department.dto;

import java.time.LocalDateTime;

/** Outbound representation of a department. */
public record DepartmentResponse(
        Long id,
        String name,
        String code,
        String description,
        String location,
        Long version,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
