package com.enterprise.payroll.department.mapper;

import com.enterprise.payroll.department.dto.DepartmentRequest;
import com.enterprise.payroll.department.dto.DepartmentResponse;
import com.enterprise.payroll.department.entity.Department;
import org.springframework.stereotype.Component;

/** Hand-written mapper - simple enough that a mapping framework would be overkill. */
@Component
public class DepartmentMapper {

    public Department toEntity(DepartmentRequest request) {
        return Department.builder()
                .name(request.name())
                .code(request.code())
                .description(request.description())
                .location(request.location())
                .build();
    }

    public void updateEntity(Department department, DepartmentRequest request) {
        department.setName(request.name());
        department.setCode(request.code());
        department.setDescription(request.description());
        department.setLocation(request.location());
    }

    public DepartmentResponse toResponse(Department department) {
        return new DepartmentResponse(
                department.getId(),
                department.getName(),
                department.getCode(),
                department.getDescription(),
                department.getLocation(),
                department.getVersion(),
                department.getCreatedAt(),
                department.getUpdatedAt()
        );
    }
}
