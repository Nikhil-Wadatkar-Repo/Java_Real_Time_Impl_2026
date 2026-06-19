package com.mco.controller;

import com.mco.dto.EmployeeDepartmentView;
import com.mco.entity.Employee;
import com.mco.service.ProductionService;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prodIssues")
@AllArgsConstructor
@CrossOrigin(origins = "*",allowedHeaders = "*")
public class ProductionIssuesController {
    private final ProductionService employeeService;

    @GetMapping("/n-plus-one-demo")
    public ResponseEntity<List<EmployeeDepartmentView>> nPlusOneDemo() {
        return ResponseEntity.ok(employeeService.findAllWithDepartmentNameNPlusOneDemo());
    }

    @GetMapping("/join-fetch-demo")
    public ResponseEntity<List<EmployeeDepartmentView>> joinFetchDemo() {
        return ResponseEntity.ok(employeeService.findAllWithDepartmentNameJoinFetch());
    }

    @GetMapping({"/paged", "/paged/{page}/{size}"})
    public ResponseEntity<Page<Employee>> getPagedEmployees(@RequestParam(name = "page", defaultValue = "0") @Min(0) int page, @RequestParam(name = "size", defaultValue = "10") @Min(1) int size, @PathVariable(name = "page", required = false) Integer pagePath, @PathVariable(name = "size", required = false) Integer sizePath) {
        int resolvedPage = pagePath != null ? pagePath : page;
        int resolvedSize = sizePath != null ? sizePath : size;
        int pageSize = Math.min(resolvedSize, 50);
        return ResponseEntity.ok(employeeService.findPaged(resolvedPage, pageSize));
    }

    @GetMapping("/asyncUpdate")
    public ResponseEntity<String> asyncUpdate() {
        return ResponseEntity.ok(employeeService.processAllEmployees());
    }
}
