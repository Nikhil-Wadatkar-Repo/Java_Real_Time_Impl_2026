package com.enterprise.payroll.project.service.impl;

import com.enterprise.payroll.project.client.EmployeeClient;
import com.enterprise.payroll.project.dto.AssignmentResponse;
import com.enterprise.payroll.project.dto.ProjectRequest;
import com.enterprise.payroll.project.dto.ProjectResponse;
import com.enterprise.payroll.project.dto.ProjectSearchCriteria;
import com.enterprise.payroll.project.entity.Project;
import com.enterprise.payroll.project.entity.ProjectAssignment;
import com.enterprise.payroll.project.exception.DuplicateResourceException;
import com.enterprise.payroll.project.exception.ExternalServiceException;
import com.enterprise.payroll.project.exception.InvalidRequestException;
import com.enterprise.payroll.project.exception.OptimisticLockConflictException;
import com.enterprise.payroll.project.exception.ResourceNotFoundException;
import com.enterprise.payroll.project.mapper.ProjectMapper;
import com.enterprise.payroll.project.repository.ProjectAssignmentRepository;
import com.enterprise.payroll.project.repository.ProjectRepository;
import com.enterprise.payroll.project.repository.spec.ProjectSpecifications;
import com.enterprise.payroll.project.service.ProjectService;
import feign.FeignException;
import jakarta.persistence.OptimisticLockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private static final Logger log = LoggerFactory.getLogger(ProjectServiceImpl.class);

    private final ProjectRepository projectRepository;
    private final ProjectAssignmentRepository projectAssignmentRepository;
    private final ProjectMapper projectMapper;
    private final EmployeeClient employeeClient;

    public ProjectServiceImpl(ProjectRepository projectRepository,
                               ProjectAssignmentRepository projectAssignmentRepository,
                               ProjectMapper projectMapper,
                               EmployeeClient employeeClient) {
        this.projectRepository = projectRepository;
        this.projectAssignmentRepository = projectAssignmentRepository;
        this.projectMapper = projectMapper;
        this.employeeClient = employeeClient;
    }

    @Override
    public ProjectResponse create(ProjectRequest request) {
        validateDateRange(request);
        if (projectRepository.existsByName(request.name())) {
            throw new DuplicateResourceException("Project already exists with name: " + request.name());
        }
        if (projectRepository.existsByCode(request.code())) {
            throw new DuplicateResourceException("Project already exists with code: " + request.code());
        }
        Project saved = projectRepository.save(projectMapper.toEntity(request));
        log.info("Created project id={} code={}", saved.getId(), saved.getCode());
        return projectMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectResponse getById(Long id) {
        return projectMapper.toResponse(findProjectOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProjectResponse> getAll(Pageable pageable) {
        return projectRepository.findAll(pageable).map(projectMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProjectResponse> search(ProjectSearchCriteria criteria, Pageable pageable) {
        return projectRepository.findAll(ProjectSpecifications.fromCriteria(criteria), pageable)
                .map(projectMapper::toResponse);
    }

    @Override
    public ProjectResponse update(Long id, ProjectRequest request, Long expectedVersion) {
        validateDateRange(request);
        Project project = findProjectOrThrow(id);

        projectRepository.findByName(request.name())
                .filter(other -> !other.getId().equals(id))
                .ifPresent(other -> {
                    throw new DuplicateResourceException("Another project already uses name: " + request.name());
                });
        projectRepository.findByCode(request.code())
                .filter(other -> !other.getId().equals(id))
                .ifPresent(other -> {
                    throw new DuplicateResourceException("Another project already uses code: " + request.code());
                });

        if (expectedVersion != null && !expectedVersion.equals(project.getVersion())) {
            throw new OptimisticLockConflictException(
                    "Project " + id + " was modified concurrently (expected version " + expectedVersion
                            + " but found " + project.getVersion() + ")");
        }

        projectMapper.updateEntity(project, request);
        try {
            Project saved = projectRepository.saveAndFlush(project);
            log.info("Updated project id={}", id);
            return projectMapper.toResponse(saved);
        } catch (OptimisticLockException ex) {
            throw new OptimisticLockConflictException("Project " + id + " was modified concurrently. Please retry.");
        }
    }

    @Override
    public void delete(Long id) {
        Project project = findProjectOrThrow(id);
        projectRepository.delete(project);
        log.info("Deleted project id={}", id);
    }

    @Override
    public AssignmentResponse assignEmployee(Long projectId, Long employeeId) {
        // Validate the cross-service reference before touching the DB, so the
        // (potentially slow) network call doesn't hold a pooled connection open.
        validateEmployeeExists(employeeId);
        Project project = findProjectOrThrow(projectId);

        if (projectAssignmentRepository.existsByProject_IdAndEmployeeId(projectId, employeeId)) {
            throw new DuplicateResourceException(
                    "Employee " + employeeId + " is already assigned to project " + projectId);
        }

        ProjectAssignment assignment = ProjectAssignment.builder()
                .project(project)
                .employeeId(employeeId)
                .build();
        ProjectAssignment saved = projectAssignmentRepository.save(assignment);
        log.info("Assigned employee id={} to project id={}", employeeId, projectId);
        return projectMapper.toAssignmentResponse(saved);
    }

    @Override
    public void removeEmployee(Long projectId, Long employeeId) {
        ProjectAssignment assignment = projectAssignmentRepository.findByProject_IdAndEmployeeId(projectId, employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee " + employeeId + " is not assigned to project " + projectId));
        projectAssignmentRepository.delete(assignment);
        log.info("Removed employee id={} from project id={}", employeeId, projectId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssignmentResponse> getAssignments(Long projectId, Pageable pageable) {
        findProjectOrThrow(projectId);
        return projectAssignmentRepository.findByProject_Id(projectId, pageable)
                .map(projectMapper::toAssignmentResponse);
    }

    private Project findProjectOrThrow(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
    }

    private void validateDateRange(ProjectRequest request) {
        if (request.endDate() != null && request.endDate().isBefore(request.startDate())) {
            throw new InvalidRequestException("endDate must not be before startDate");
        }
    }

    /**
     * Confirms {@code employeeId} refers to a real employee before we
     * commit the assignment. A 404 from employee-service means the caller's
     * fault (bad request); anything else (timeout, connection refused, 5xx)
     * means the dependency is unavailable - surfaced as 503 rather than
     * silently accepting an unverified assignment.
     */
    private void validateEmployeeExists(Long employeeId) {
        try {
            employeeClient.getEmployeeById(employeeId);
        } catch (FeignException.NotFound ex) {
            throw new InvalidRequestException("Employee not found with id: " + employeeId);
        } catch (FeignException ex) {
            log.error("employee-service call failed for employeeId={}", employeeId, ex);
            throw new ExternalServiceException("employee-service is currently unavailable", ex);
        }
    }
}
