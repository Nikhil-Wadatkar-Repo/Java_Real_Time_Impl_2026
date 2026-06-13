package com.mco.controller;

import com.mco.dto.EmployeeDepartmentView;
import com.mco.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/prodIssues")
@AllArgsConstructor
public class ProductionIssuesController {
    private final EmployeeService employeeService;

    @GetMapping("/n-plus-one-demo")
    public ResponseEntity<List<EmployeeDepartmentView>> nPlusOneDemo() {
        return ResponseEntity.ok(employeeService.findAllWithDepartmentNameNPlusOneDemo());
    }

    @GetMapping("/join-fetch-demo")
    public ResponseEntity<List<EmployeeDepartmentView>> joinFetchDemo() {
        return ResponseEntity.ok(employeeService.findAllWithDepartmentNameJoinFetch());
    }
}
