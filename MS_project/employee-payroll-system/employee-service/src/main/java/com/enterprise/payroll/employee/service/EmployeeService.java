package com.enterprise.payroll.employee.service;

import com.enterprise.payroll.employee.dto.EmployeeRequest;
import com.enterprise.payroll.employee.dto.EmployeeResponse;
import com.enterprise.payroll.employee.dto.EmployeeSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {

    EmployeeResponse create(EmployeeRequest request);

    EmployeeResponse getById(Long id);

    Page<EmployeeResponse> getAll(Pageable pageable);

    Page<EmployeeResponse> search(EmployeeSearchCriteria criteria, Pageable pageable);

    EmployeeResponse update(Long id, EmployeeRequest request, Long expectedVersion);

    void delete(Long id);
}
