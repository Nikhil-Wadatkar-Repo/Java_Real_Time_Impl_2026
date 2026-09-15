package com.enterprise.payroll.project.controller;

import com.enterprise.payroll.project.dto.AssignmentResponse;
import com.enterprise.payroll.project.dto.ProjectRequest;
import com.enterprise.payroll.project.dto.ProjectResponse;
import com.enterprise.payroll.project.dto.ProjectSearchCriteria;
import com.enterprise.payroll.project.entity.ProjectStatus;
import com.enterprise.payroll.project.service.ProjectService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private static final Logger log = LoggerFactory.getLogger(ProjectController.class);

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<ProjectResponse> create(@Valid @RequestBody ProjectRequest request) {
        log.info("Received request to create project code={}", request.code());
        ProjectResponse response = projectService.create(request);
        return ResponseEntity.created(URI.create("/projects/" + response.id())).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getById(id));
    }

    /** GET /projects?page=0&size=20&sort=startDate,desc */
    @GetMapping
    public ResponseEntity<Page<ProjectResponse>> getAll(Pageable pageable) {
        return ResponseEntity.ok(projectService.getAll(pageable));
    }

    /** GET /projects/search?name=&code=&status=&startedAfter=&startedBefore= - all filters optional and combinable. */
    @GetMapping("/search")
    public ResponseEntity<Page<ProjectResponse>> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) ProjectStatus status,
            @RequestParam(required = false) LocalDate startedAfter,
            @RequestParam(required = false) LocalDate startedBefore,
            Pageable pageable) {
        ProjectSearchCriteria criteria = new ProjectSearchCriteria(name, code, status, startedAfter, startedBefore);
        return ResponseEntity.ok(projectService.search(criteria, pageable));
    }

    /** PUT /projects/{id}?ifMatchVersion=3 - see employee-service's controller for the rationale. */
    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ProjectRequest request,
            @RequestParam(required = false) Long ifMatchVersion) {
        return ResponseEntity.ok(projectService.update(id, request, ifMatchVersion));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        projectService.delete(id);
    }

    @PostMapping("/{projectId}/employees/{employeeId}")
    public ResponseEntity<AssignmentResponse> assignEmployee(@PathVariable Long projectId, @PathVariable Long employeeId) {
        log.info("Received request to assign employee id={} to project id={}", employeeId, projectId);
        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.assignEmployee(projectId, employeeId));
    }

    @DeleteMapping("/{projectId}/employees/{employeeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeEmployee(@PathVariable Long projectId, @PathVariable Long employeeId) {
        projectService.removeEmployee(projectId, employeeId);
    }

    /** GET /projects/{projectId}/employees?page=0&size=20 - assigned employee ids, paginated. */
    @GetMapping("/{projectId}/employees")
    public ResponseEntity<Page<AssignmentResponse>> getAssignments(@PathVariable Long projectId, Pageable pageable) {
        return ResponseEntity.ok(projectService.getAssignments(projectId, pageable));
    }
}
