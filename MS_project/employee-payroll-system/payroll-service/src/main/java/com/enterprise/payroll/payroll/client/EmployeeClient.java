package com.enterprise.payroll.payroll.client;

import com.enterprise.payroll.payroll.client.dto.EmployeeDto;
import com.enterprise.payroll.payroll.client.dto.PageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "employee-service")
public interface EmployeeClient {

    @GetMapping("/employees/{id}")
    EmployeeDto getEmployeeById(@PathVariable("id") Long id);

    /** Used by POST /payroll/process-month to discover who to run payroll for. */
    @GetMapping("/employees/search")
    PageResponse<EmployeeDto> searchEmployees(@RequestParam("status") String status,
                                               @RequestParam("size") int size);
}
