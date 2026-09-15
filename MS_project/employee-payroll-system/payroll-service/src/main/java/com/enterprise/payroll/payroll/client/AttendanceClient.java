package com.enterprise.payroll.payroll.client;

import com.enterprise.payroll.payroll.client.dto.AttendanceSummaryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "attendance-service")
public interface AttendanceClient {

    @GetMapping("/attendance/{employeeId}/summary")
    AttendanceSummaryDto getSummary(@PathVariable("employeeId") Long employeeId, @RequestParam("month") String month);
}
