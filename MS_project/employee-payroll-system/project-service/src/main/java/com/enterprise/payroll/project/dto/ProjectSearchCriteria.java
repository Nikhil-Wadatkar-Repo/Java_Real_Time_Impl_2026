package com.enterprise.payroll.project.dto;

import com.enterprise.payroll.project.entity.ProjectStatus;

import java.time.LocalDate;

/**
 * Optional filters for GET /projects/search. Every field is nullable;
 * only supplied ones become a predicate.
 */
public record ProjectSearchCriteria(
        String name,
        String code,
        ProjectStatus status,
        LocalDate startedAfter,
        LocalDate startedBefore
) {
}
