package com.enterprise.payroll.employee.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Employee-service's own, minimal view of a department, as returned by
 * department-service's API. Deliberately not the same class as
 * department-service's own {@code DepartmentResponse} - each service owns
 * its contract with the outside world, and this client only needs enough
 * fields to validate a reference and (later) enrich a response.
 * {@code @JsonIgnoreProperties(ignoreUnknown = true)} because
 * department-service's actual payload has more fields (description,
 * location, version, timestamps) that this consumer doesn't care about -
 * without it, a field added upstream would break deserialization here.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record DepartmentDto(
        Long id,
        String name,
        String code
) {
}
