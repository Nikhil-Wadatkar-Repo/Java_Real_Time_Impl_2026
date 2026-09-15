package com.enterprise.payroll.employee.service.impl;

import com.enterprise.payroll.employee.client.DepartmentClient;
import com.enterprise.payroll.employee.dto.EmployeeRequest;
import com.enterprise.payroll.employee.dto.EmployeeResponse;
import com.enterprise.payroll.employee.dto.EmployeeSearchCriteria;
import com.enterprise.payroll.employee.entity.Employee;
import com.enterprise.payroll.employee.exception.DuplicateResourceException;
import com.enterprise.payroll.employee.exception.ExternalServiceException;
import com.enterprise.payroll.employee.exception.InvalidRequestException;
import com.enterprise.payroll.employee.exception.OptimisticLockConflictException;
import com.enterprise.payroll.employee.exception.ResourceNotFoundException;
import com.enterprise.payroll.employee.mapper.EmployeeMapper;
import com.enterprise.payroll.employee.repository.EmployeeRepository;
import com.enterprise.payroll.employee.repository.spec.EmployeeSpecifications;
import com.enterprise.payroll.employee.service.EmployeeService;
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
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final DepartmentClient departmentClient;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper,
                                DepartmentClient departmentClient) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.departmentClient = departmentClient;
    }

    @Override
    public EmployeeResponse create(EmployeeRequest request) {
        // Validate the cross-service reference before touching the DB, so the
        // (potentially slow) network call doesn't hold a pooled connection open.
        validateDepartmentExists(request.departmentId());
        if (employeeRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Employee already exists with email: " + request.email());
        }
        Employee saved = employeeRepository.save(employeeMapper.toEntity(request));
        log.info("Created employee id={} email={}", saved.getId(), saved.getEmail());
        return employeeMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponse getById(Long id) {
        return employeeMapper.toResponse(findOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeResponse> getAll(Pageable pageable) {
        return employeeRepository.findAll(pageable).map(employeeMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeResponse> search(EmployeeSearchCriteria criteria, Pageable pageable) {
        return employeeRepository.findAll(EmployeeSpecifications.fromCriteria(criteria), pageable)
                .map(employeeMapper::toResponse);
    }

    @Override
    public EmployeeResponse update(Long id, EmployeeRequest request, Long expectedVersion) {
        validateDepartmentExists(request.departmentId());
        Employee employee = findOrThrow(id);

        employeeRepository.findByEmail(request.email())
                .filter(other -> !other.getId().equals(id))
                .ifPresent(other -> {
                    throw new DuplicateResourceException("Another employee already uses email: " + request.email());
                });

        if (expectedVersion != null && !expectedVersion.equals(employee.getVersion())) {
            throw new OptimisticLockConflictException(
                    "Employee " + id + " was modified concurrently (expected version " + expectedVersion
                            + " but found " + employee.getVersion() + ")");
        }

        employeeMapper.updateEntity(employee, request);
        try {
            Employee saved = employeeRepository.saveAndFlush(employee);
            log.info("Updated employee id={}", id);
            return employeeMapper.toResponse(saved);
        } catch (OptimisticLockException ex) {
            throw new OptimisticLockConflictException("Employee " + id + " was modified concurrently. Please retry.");
        }
    }

    @Override
    public void delete(Long id) {
        Employee employee = findOrThrow(id);
        employeeRepository.delete(employee);
        log.info("Deleted employee id={}", id);
    }

    private Employee findOrThrow(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
    }

    /**
     * Confirms {@code departmentId} refers to a real department before we
     * commit to it. A 404 from department-service means the caller's fault
     * (bad request); anything else (timeout, connection refused, 5xx) means
     * the dependency is unavailable and we shouldn't guess - surface it as
     * a 503 rather than silently accepting an unverified reference.
     */
    private void validateDepartmentExists(Long departmentId) {
        try {
            departmentClient.getDepartmentById(departmentId);
        } catch (FeignException.NotFound ex) {
            throw new InvalidRequestException("Department not found with id: " + departmentId);
        } catch (FeignException ex) {
            log.error("department-service call failed for departmentId={}", departmentId, ex);
            throw new ExternalServiceException("department-service is currently unavailable", ex);
        }
    }
}
