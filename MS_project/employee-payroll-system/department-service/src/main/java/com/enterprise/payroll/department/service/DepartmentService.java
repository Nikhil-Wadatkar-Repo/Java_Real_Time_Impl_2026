package com.enterprise.payroll.department.service;

import com.enterprise.payroll.department.dto.DepartmentRequest;
import com.enterprise.payroll.department.dto.DepartmentResponse;
import com.enterprise.payroll.department.dto.DepartmentSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DepartmentService {

    DepartmentResponse create(DepartmentRequest request);

    DepartmentResponse getById(Long id);

    Page<DepartmentResponse> getAll(Pageable pageable);

    Page<DepartmentResponse> search(DepartmentSearchCriteria criteria, Pageable pageable);

    DepartmentResponse update(Long id, DepartmentRequest request, Long expectedVersion);

    void delete(Long id);
}
