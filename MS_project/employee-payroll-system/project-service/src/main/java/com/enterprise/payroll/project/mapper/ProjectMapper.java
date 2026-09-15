package com.enterprise.payroll.project.mapper;

import com.enterprise.payroll.project.dto.AssignmentResponse;
import com.enterprise.payroll.project.dto.ProjectRequest;
import com.enterprise.payroll.project.dto.ProjectResponse;
import com.enterprise.payroll.project.entity.Project;
import com.enterprise.payroll.project.entity.ProjectAssignment;
import org.springframework.stereotype.Component;

/** Hand-written mapper - simple enough that a mapping framework would be overkill. */
@Component
public class ProjectMapper {

    public Project toEntity(ProjectRequest request) {
        return Project.builder()
                .name(request.name())
                .code(request.code())
                .description(request.description())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .status(request.status())
                .build();
    }

    public void updateEntity(Project project, ProjectRequest request) {
        project.setName(request.name());
        project.setCode(request.code());
        project.setDescription(request.description());
        project.setStartDate(request.startDate());
        project.setEndDate(request.endDate());
        project.setStatus(request.status());
    }

    public ProjectResponse toResponse(Project project) {
        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getCode(),
                project.getDescription(),
                project.getStartDate(),
                project.getEndDate(),
                project.getStatus(),
                project.getVersion(),
                project.getCreatedAt(),
                project.getUpdatedAt()
        );
    }

    public AssignmentResponse toAssignmentResponse(ProjectAssignment assignment) {
        return new AssignmentResponse(
                assignment.getProject().getId(),
                assignment.getEmployeeId(),
                assignment.getAssignedAt()
        );
    }
}
