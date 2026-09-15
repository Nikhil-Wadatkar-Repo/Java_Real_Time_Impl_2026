package com.enterprise.payroll.employee.config;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Propagates the inbound X-Correlation-Id header onto every outgoing Feign
 * call, so a single id can be followed gateway -> employee-service ->
 * department-service in the logs (same header the gateway's
 * CorrelationIdFilter guarantees is already present on the inbound request).
 */
@Configuration
public class FeignClientConfig {

    private static final String CORRELATION_ID_HEADER = "X-Correlation-Id";

    @Bean
    public RequestInterceptor correlationIdForwardingInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes attributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes == null) {
                return;
            }
            HttpServletRequest currentRequest = attributes.getRequest();
            String correlationId = currentRequest.getHeader(CORRELATION_ID_HEADER);
            if (correlationId != null && !correlationId.isBlank()) {
                requestTemplate.header(CORRELATION_ID_HEADER, correlationId);
            }
        };
    }
}
