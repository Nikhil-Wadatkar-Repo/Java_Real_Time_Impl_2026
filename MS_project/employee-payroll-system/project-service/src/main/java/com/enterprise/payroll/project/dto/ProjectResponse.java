package com.enterprise.payroll.project.dto;

import com.enterprise.payroll.project.entity.ProjectStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

/** Outbound representation of a project. */
public record ProjectResponse(
        Long id,
        String name,
        String code,
        String description,
        LocalDate startDate,
        LocalDate endDate,
        ProjectStatus status,
        Long version,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
