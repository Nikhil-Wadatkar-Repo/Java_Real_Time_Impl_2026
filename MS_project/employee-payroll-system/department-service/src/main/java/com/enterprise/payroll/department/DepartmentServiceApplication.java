package com.enterprise.payroll.department;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Owns department master data: CRUD and search. Employee, Project and
 * Attendance services only ever hold a {@code departmentId} reference to
 * this service's data - never a direct DB join.
 */
@SpringBootApplication
public class DepartmentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DepartmentServiceApplication.class, args);
    }
}
