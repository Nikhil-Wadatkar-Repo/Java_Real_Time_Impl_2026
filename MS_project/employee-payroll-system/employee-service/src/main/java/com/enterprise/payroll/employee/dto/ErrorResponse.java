package com.enterprise.payroll.employee.dto;

import java.time.Instant;
import java.util.List;

/**
 * Standard error contract returned by every endpoint in this service (and,
 * by convention, every other service in the platform) so clients can
 * handle failures uniformly regardless of which microservice answered.
 */
public record ErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        String traceId,
        List<String> details
) {
}
