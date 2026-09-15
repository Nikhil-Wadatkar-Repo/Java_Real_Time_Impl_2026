package com.enterprise.payroll.employee.client;

import com.enterprise.payroll.employee.client.dto.DepartmentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Synchronous call to department-service, resolved via Eureka using the
 * logical service name (no hardcoded host/port). Resilience (timeout,
 * retry, circuit breaker, fallback) is layered on top of this interface in
 * Phase 9 - for now only a bounded connect/read timeout is configured
 * (see application.yml) so a slow department-service can't hang an
 * employee create/update request forever.
 */
@FeignClient(name = "department-service")
public interface DepartmentClient {

    @GetMapping("/departments/{id}")
    DepartmentDto getDepartmentById(@PathVariable("id") Long id);
}
