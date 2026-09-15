package com.enterprise.payroll.payroll.config;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
