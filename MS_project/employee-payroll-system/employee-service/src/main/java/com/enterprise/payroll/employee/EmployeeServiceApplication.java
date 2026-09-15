package com.enterprise.payroll.employee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Owns employee master data: CRUD, search, salary, status and the
 * (loosely coupled, id-only) reference to a department. Validates that
 * reference synchronously via a Feign call to department-service on
 * create/update - see {@link com.enterprise.payroll.employee.client.DepartmentClient}.
 */
@SpringBootApplication
@EnableFeignClients
public class EmployeeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployeeServiceApplication.class, args);
    }
}
