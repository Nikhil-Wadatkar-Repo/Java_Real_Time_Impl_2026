package com.enterprise.payroll.department.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Inbound payload for creating/updating a department. */
public record DepartmentRequest(

        @NotBlank(message = "Name is required")
        @Size(max = 150, message = "Name must not exceed 150 characters")
        String name,

        @NotBlank(message = "Code is required")
        @Size(max = 20, message = "Code must not exceed 20 characters")
        String code,

        @Size(max = 500, message = "Description must not exceed 500 characters")
        String description,

        @Size(max = 150, message = "Location must not exceed 150 characters")
        String location
) {
}
