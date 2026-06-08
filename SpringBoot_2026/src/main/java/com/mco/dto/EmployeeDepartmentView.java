package com.mco.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmployeeDepartmentView {

    private final Long employeeId;
    private final String employeeName;
    private final String departmentName;
}
