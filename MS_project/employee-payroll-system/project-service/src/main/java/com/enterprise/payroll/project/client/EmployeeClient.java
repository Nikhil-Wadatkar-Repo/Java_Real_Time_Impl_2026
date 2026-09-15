package com.enterprise.payroll.project.client;

import com.enterprise.payroll.project.client.dto.EmployeeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Synchronous call to employee-service, resolved via Eureka using the
 * logical service name. Same pattern (and same resilience posture - bounded
 * timeout only, Resilience4j lands in Phase 9) as employee-service's own
 * {@code DepartmentClient}.
 */
@FeignClient(name = "employee-service")
public interface EmployeeClient {

    @GetMapping("/employees/{id}")
    EmployeeDto getEmployeeById(@PathVariable("id") Long id);
}
