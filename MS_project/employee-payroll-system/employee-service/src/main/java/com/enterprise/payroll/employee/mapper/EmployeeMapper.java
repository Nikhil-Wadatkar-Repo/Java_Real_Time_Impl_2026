package com.enterprise.payroll.employee.mapper;

import com.enterprise.payroll.employee.dto.EmployeeRequest;
import com.enterprise.payroll.employee.dto.EmployeeResponse;
import com.enterprise.payroll.employee.entity.Employee;
import org.springframework.stereotype.Component;

/** Hand-written mapper - simple enough that a mapping framework would be overkill. */
@Component
public class EmployeeMapper {

    public Employee toEntity(EmployeeRequest request) {
        return Employee.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .departmentId(request.departmentId())
                .designation(request.designation())
                .salary(request.salary())
                .status(request.status())
                .joiningDate(request.joiningDate())
                .build();
    }

    public void updateEntity(Employee employee, EmployeeRequest request) {
        employee.setFirstName(request.firstName());
        employee.setLastName(request.lastName());
        employee.setEmail(request.email());
        employee.setDepartmentId(request.departmentId());
        employee.setDesignation(request.designation());
        employee.setSalary(request.salary());
        employee.setStatus(request.status());
        employee.setJoiningDate(request.joiningDate());
    }

    public EmployeeResponse toResponse(Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getDepartmentId(),
                employee.getDesignation(),
                employee.getSalary(),
                employee.getStatus(),
                employee.getJoiningDate(),
                employee.getVersion(),
                employee.getCreatedAt(),
                employee.getUpdatedAt()
        );
    }
}
