package com.enterprise.payroll.payroll.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Minimal mirror of Spring Data's default {@code Page} JSON shape (which
 * has many more fields - pageable, sort, first/last, etc.) - this consumer
 * only needs {@code content}, so everything else is ignored rather than
 * modeled.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record PageResponse<T>(List<T> content) {
}
