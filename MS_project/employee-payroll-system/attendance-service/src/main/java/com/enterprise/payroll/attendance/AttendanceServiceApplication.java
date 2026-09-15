package com.enterprise.payroll.attendance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Tracks employee check-in/check-out and derives daily/monthly working
 * hours. Payroll Service calls this service (via Feign) to get the
 * attendance data it needs to compute salary for a given month.
 */
@SpringBootApplication
@EnableFeignClients
public class AttendanceServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AttendanceServiceApplication.class, args);
    }
}
