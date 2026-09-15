package com.enterprise.payroll.attendance.client;

import com.enterprise.payroll.attendance.client.dto.EmployeeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "employee-service")
public interface EmployeeClient {

    @GetMapping("/employees/{id}")
    EmployeeDto getEmployeeById(@PathVariable("id") Long id);
}
