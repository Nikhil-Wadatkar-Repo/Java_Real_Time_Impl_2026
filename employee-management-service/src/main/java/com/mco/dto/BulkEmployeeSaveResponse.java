package com.mco.dto;

import java.util.List;

import com.mco.entity.Employee;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BulkEmployeeSaveResponse {

    private final int requestedCount;
    private final int savedCount;
    private final int skippedCount;
    private final List<Employee> savedEmployees;
    private final List<String> skippedReasons;
}
