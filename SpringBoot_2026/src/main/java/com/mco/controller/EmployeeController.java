package com.mco.controller;

import java.util.List;

import com.mco.dto.BulkEmployeeSaveResponse;
import com.mco.dto.EmployeeDepartmentView;
import com.mco.entity.Employee;
import com.mco.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<Employee> create(@Valid @RequestBody Employee employee) {
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.create(employee));
    }

    @PostMapping("/bulk")
    public ResponseEntity<BulkEmployeeSaveResponse> createBulk(@Valid @RequestBody List<@Valid Employee> employees) {
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.createBulk(employees));
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAll() {
        return ResponseEntity.ok(employeeService.findAll());
    }

    @GetMapping("/n-plus-one-demo")
    public ResponseEntity<List<EmployeeDepartmentView>> nPlusOneDemo() {
        return ResponseEntity.ok(employeeService.findAllWithDepartmentNameNPlusOneDemo());
    }

    @GetMapping("/join-fetch-demo")
    public ResponseEntity<List<EmployeeDepartmentView>> joinFetchDemo() {
        return ResponseEntity.ok(employeeService.findAllWithDepartmentNameJoinFetch());
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<Employee> getById(@PathVariable Long employeeId) {
        return ResponseEntity.ok(employeeService.findById(employeeId));
    }

    @PutMapping("/{employeeId}")
    public ResponseEntity<Employee> update(@PathVariable Long employeeId, @Valid @RequestBody Employee employee) {
        return ResponseEntity.ok(employeeService.update(employeeId, employee));
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<Void> delete(@PathVariable Long employeeId) {
        employeeService.delete(employeeId);
        return ResponseEntity.noContent().build();
    }


}
