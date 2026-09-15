package com.enterprise.payroll.reporting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Generates reports (payroll summaries for now) asynchronously: the
 * triggering POST returns 202 Accepted immediately with a report id, and a
 * background thread pool (see {@code ReportAsyncConfig}) does the actual
 * Feign call + aggregation. Callers poll GET /reports/{id} for the result.
 */
@SpringBootApplication
@EnableFeignClients
@EnableAsync
public class ReportingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReportingServiceApplication.class, args);
    }
}
