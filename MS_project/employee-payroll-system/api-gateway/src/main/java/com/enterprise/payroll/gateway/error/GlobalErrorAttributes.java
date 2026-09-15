package com.enterprise.payroll.gateway.error;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.time.Instant;
import java.util.Map;

/**
 * Normalizes gateway-level failures (routing failures, downstream timeouts,
 * connection refused, circuit breaker open) into the same error response
 * shape the business services return, so clients see one consistent contract.
 */
@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> attributes = super.getErrorAttributes(request, options);
        Map<String, Object> body = Map.of(
                "timestamp", Instant.now().toString(),
                "status", attributes.getOrDefault("status", 500),
                "error", attributes.getOrDefault("error", "GATEWAY_ERROR"),
                "message", attributes.getOrDefault("message", "Service temporarily unavailable"),
                "path", attributes.getOrDefault("path", request.path())
        );
        return body;
    }
}
