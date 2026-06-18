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

    @PostMapping("/seed-another-30000")
    public ResponseEntity<BulkSeedResponse> seedAnother30000Employees() {
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.seedAnother30000Employees());
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAll() {
        return ResponseEntity.ok(employeeService.findAll());
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

    @GetMapping("/getById/{id}")
    public ResponseEntity<Employee> getById(@PathVariable("id")Long employeeId){
        return new ResponseEntity<>(employeeService.findById(employeeId),HttpStatus.OK);
    }
}
