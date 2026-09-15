package com.enterprise.payroll.reporting.client;

import com.enterprise.payroll.reporting.client.dto.PageResponse;
import com.enterprise.payroll.reporting.client.dto.PayrollDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "payroll-service")
public interface PayrollClient {

    @GetMapping("/payroll")
    PageResponse<PayrollDto> getPayrollsForMonth(@RequestParam("month") String month,
                                                  @RequestParam("size") int size);
}
