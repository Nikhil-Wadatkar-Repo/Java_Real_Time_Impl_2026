package com.enterprise.payroll.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Owns project master data and employee-to-project assignments. Validates
 * assigned employee ids synchronously via a Feign call to employee-service
 * - see {@link com.enterprise.payroll.project.client.EmployeeClient}, the
 * same pattern employee-service uses against department-service.
 */
@SpringBootApplication
@EnableFeignClients
public class ProjectServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectServiceApplication.class, args);
    }
}
