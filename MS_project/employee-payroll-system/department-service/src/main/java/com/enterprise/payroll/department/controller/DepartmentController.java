package com.enterprise.payroll.department.controller;

import com.enterprise.payroll.department.dto.DepartmentRequest;
import com.enterprise.payroll.department.dto.DepartmentResponse;
import com.enterprise.payroll.department.dto.DepartmentSearchCriteria;
import com.enterprise.payroll.department.service.DepartmentService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    private static final Logger log = LoggerFactory.getLogger(DepartmentController.class);

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping
    public ResponseEntity<DepartmentResponse> create(@Valid @RequestBody DepartmentRequest request) {
        log.info("Received request to create department code={}", request.code());
        DepartmentResponse response = departmentService.create(request);
        return ResponseEntity.created(URI.create("/departments/" + response.id())).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(departmentService.getById(id));
    }

    /** GET /departments?page=0&size=20&sort=name,asc */
    @GetMapping
    public ResponseEntity<Page<DepartmentResponse>> getAll(Pageable pageable) {
        return ResponseEntity.ok(departmentService.getAll(pageable));
    }

    /** GET /departments/search?name=&code=&location= - all filters optional and combinable. */
    @GetMapping("/search")
    public ResponseEntity<Page<DepartmentResponse>> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String location,
            Pageable pageable) {
        DepartmentSearchCriteria criteria = new DepartmentSearchCriteria(name, code, location);
        return ResponseEntity.ok(departmentService.search(criteria, pageable));
    }

    /** PUT /departments/{id}?ifMatchVersion=3 - see employee-service's controller for the rationale. */
    @PutMapping("/{id}")
    public ResponseEntity<DepartmentResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody DepartmentRequest request,
            @RequestParam(required = false) Long ifMatchVersion) {
        return ResponseEntity.ok(departmentService.update(id, request, ifMatchVersion));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        departmentService.delete(id);
    }
}
