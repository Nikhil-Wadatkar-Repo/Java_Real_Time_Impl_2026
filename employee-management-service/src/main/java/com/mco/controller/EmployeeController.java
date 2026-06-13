package com.mco.controller;

import java.util.List;

import com.mco.dto.BulkSeedResponse;
import com.mco.dto.BulkEmployeeSaveResponse;
import com.mco.entity.Employee;
import com.mco.service.EmployeeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employeeAPI")
@Validated
@CrossOrigin(origins = "*",allowedHeaders = "*")
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

    @PostMapping("/seed-real-time")
    public ResponseEntity<BulkSeedResponse> seedRealTimeEmployees(@RequestParam(defaultValue = "20000") @Min(1) int count) {
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.seedRealTimeEmployees(count));
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAll() {
        return ResponseEntity.ok(employeeService.findAll());
    }

    @GetMapping({"/paged", "/paged/{page}/{size}"})
    public ResponseEntity<Page<Employee>> getPagedEmployees(
            @RequestParam(name = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(name = "size", defaultValue = "10") @Min(1) int size,
            @PathVariable(name = "page", required = false) Integer pagePath,
            @PathVariable(name = "size", required = false) Integer sizePath) {
        int resolvedPage = pagePath != null ? pagePath : page;
        int resolvedSize = sizePath != null ? sizePath : size;
        int pageSize = Math.min(resolvedSize, 50);
        return ResponseEntity.ok(employeeService.findPaged(resolvedPage, pageSize));
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
