package com.enterprise.payroll.department.dto;

import java.time.Instant;
import java.util.List;

/**
 * Standard error contract - identical shape to every other service in the
 * platform (see employee-service's copy) so clients handle failures the
 * same way regardless of which microservice answered.
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
