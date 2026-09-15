package com.enterprise.payroll.employee.controller;

import com.enterprise.payroll.employee.dto.EmployeeRequest;
import com.enterprise.payroll.employee.dto.EmployeeResponse;
import com.enterprise.payroll.employee.dto.EmployeeSearchCriteria;
import com.enterprise.payroll.employee.entity.EmployeeStatus;
import com.enterprise.payroll.employee.service.EmployeeService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private static final Logger log = LoggerFactory.getLogger(EmployeeController.class);

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<EmployeeResponse> create(@Valid @RequestBody EmployeeRequest request) {
        log.info("Received request to create employee email={}", request.email());
        EmployeeResponse response = employeeService.create(request);
        return ResponseEntity.created(URI.create("/employees/" + response.id())).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getById(id));
    }

    /** GET /employees?page=0&size=20&sort=salary,desc */
    @GetMapping
    public ResponseEntity<Page<EmployeeResponse>> getAll(Pageable pageable) {
        return ResponseEntity.ok(employeeService.getAll(pageable));
    }

    /**
     * GET /employees/search?name=&departmentId=&status=&minSalary=&maxSalary=&joinedAfter=&joinedBefore=
     * All filters optional and combinable; results are paginated to avoid loading the whole table.
     */
    @GetMapping("/search")
    public ResponseEntity<Page<EmployeeResponse>> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) EmployeeStatus status,
            @RequestParam(required = false) BigDecimal minSalary,
            @RequestParam(required = false) BigDecimal maxSalary,
            @RequestParam(required = false) LocalDate joinedAfter,
            @RequestParam(required = false) LocalDate joinedBefore,
            Pageable pageable) {
        EmployeeSearchCriteria criteria = new EmployeeSearchCriteria(
                name, departmentId, status, minSalary, maxSalary, joinedAfter, joinedBefore);
        return ResponseEntity.ok(employeeService.search(criteria, pageable));
    }

    /**
     * PUT /employees/{id}?ifMatchVersion=3 - the optional version param lets a
     * caller opt in to explicit optimistic-lock checking; Hibernate's own
     * @Version check still guards against a lost update either way.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequest request,
            @RequestParam(required = false) Long ifMatchVersion) {
        return ResponseEntity.ok(employeeService.update(id, request, ifMatchVersion));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        employeeService.delete(id);
    }
}
