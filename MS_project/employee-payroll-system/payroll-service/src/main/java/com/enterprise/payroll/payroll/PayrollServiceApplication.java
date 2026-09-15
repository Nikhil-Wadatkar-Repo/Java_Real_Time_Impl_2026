package com.enterprise.payroll.payroll;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Computes and stores monthly salary. Calls employee-service (for salary
 * and status) and attendance-service (for the month's attendance summary)
 * synchronously via Feign, then publishes a {@code payroll.completed}
 * Kafka event so notification-service can react without payroll blocking
 * on it.
 */
@SpringBootApplication
@EnableFeignClients
public class PayrollServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PayrollServiceApplication.class, args);
    }
}
