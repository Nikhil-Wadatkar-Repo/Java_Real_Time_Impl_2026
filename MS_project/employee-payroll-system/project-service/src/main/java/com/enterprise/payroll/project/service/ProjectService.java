package com.enterprise.payroll.project.service;

import com.enterprise.payroll.project.dto.AssignmentResponse;
import com.enterprise.payroll.project.dto.ProjectRequest;
import com.enterprise.payroll.project.dto.ProjectResponse;
import com.enterprise.payroll.project.dto.ProjectSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjectService {

    ProjectResponse create(ProjectRequest request);

    ProjectResponse getById(Long id);

    Page<ProjectResponse> getAll(Pageable pageable);

    Page<ProjectResponse> search(ProjectSearchCriteria criteria, Pageable pageable);

    ProjectResponse update(Long id, ProjectRequest request, Long expectedVersion);

    void delete(Long id);

    AssignmentResponse assignEmployee(Long projectId, Long employeeId);

    void removeEmployee(Long projectId, Long employeeId);

    Page<AssignmentResponse> getAssignments(Long projectId, Pageable pageable);
}
