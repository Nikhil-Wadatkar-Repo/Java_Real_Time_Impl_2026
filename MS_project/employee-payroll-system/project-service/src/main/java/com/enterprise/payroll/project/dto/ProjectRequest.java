package com.enterprise.payroll.project.dto;

import com.enterprise.payroll.project.entity.ProjectStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/** Inbound payload for creating/updating a project. */
public record ProjectRequest(

        @NotBlank(message = "Name is required")
        @Size(max = 150, message = "Name must not exceed 150 characters")
        String name,

        @NotBlank(message = "Code is required")
        @Size(max = 20, message = "Code must not exceed 20 characters")
        String code,

        @Size(max = 500, message = "Description must not exceed 500 characters")
        String description,

        @NotNull(message = "Start date is required")
        LocalDate startDate,

        LocalDate endDate,

        @NotNull(message = "Status is required")
        ProjectStatus status
) {
}
