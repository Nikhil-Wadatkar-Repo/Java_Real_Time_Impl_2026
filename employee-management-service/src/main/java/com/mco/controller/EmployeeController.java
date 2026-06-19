package com.mco.controller;

import java.util.List;

import com.mco.entity.Employee;
import com.mco.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employeeAPI")
@Validated
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<Employee> create(@Valid @RequestBody Employee employee) {
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.create(employee));
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
    public ResponseEntity<Employee> getById(@PathVariable("id") Long employeeId) {
        return new ResponseEntity<>(employeeService.findById(employeeId), HttpStatus.OK);
    }
}
