package com.enterprise.payroll.department.service.impl;

import com.enterprise.payroll.department.dto.DepartmentRequest;
import com.enterprise.payroll.department.dto.DepartmentResponse;
import com.enterprise.payroll.department.dto.DepartmentSearchCriteria;
import com.enterprise.payroll.department.entity.Department;
import com.enterprise.payroll.department.exception.DuplicateResourceException;
import com.enterprise.payroll.department.exception.OptimisticLockConflictException;
import com.enterprise.payroll.department.exception.ResourceNotFoundException;
import com.enterprise.payroll.department.mapper.DepartmentMapper;
import com.enterprise.payroll.department.repository.DepartmentRepository;
import com.enterprise.payroll.department.repository.spec.DepartmentSpecifications;
import com.enterprise.payroll.department.service.DepartmentService;
import jakarta.persistence.OptimisticLockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    private static final Logger log = LoggerFactory.getLogger(DepartmentServiceImpl.class);

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository, DepartmentMapper departmentMapper) {
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
    }

    @Override
    public DepartmentResponse create(DepartmentRequest request) {
        if (departmentRepository.existsByName(request.name())) {
            throw new DuplicateResourceException("Department already exists with name: " + request.name());
        }
        if (departmentRepository.existsByCode(request.code())) {
            throw new DuplicateResourceException("Department already exists with code: " + request.code());
        }
        Department saved = departmentRepository.save(departmentMapper.toEntity(request));
        log.info("Created department id={} code={}", saved.getId(), saved.getCode());
        return departmentMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentResponse getById(Long id) {
        return departmentMapper.toResponse(findOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DepartmentResponse> getAll(Pageable pageable) {
        return departmentRepository.findAll(pageable).map(departmentMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DepartmentResponse> search(DepartmentSearchCriteria criteria, Pageable pageable) {
        return departmentRepository.findAll(DepartmentSpecifications.fromCriteria(criteria), pageable)
                .map(departmentMapper::toResponse);
    }

    @Override
    public DepartmentResponse update(Long id, DepartmentRequest request, Long expectedVersion) {
        Department department = findOrThrow(id);

        departmentRepository.findByName(request.name())
                .filter(other -> !other.getId().equals(id))
                .ifPresent(other -> {
                    throw new DuplicateResourceException("Another department already uses name: " + request.name());
                });
        departmentRepository.findByCode(request.code())
                .filter(other -> !other.getId().equals(id))
                .ifPresent(other -> {
                    throw new DuplicateResourceException("Another department already uses code: " + request.code());
                });

        if (expectedVersion != null && !expectedVersion.equals(department.getVersion())) {
            throw new OptimisticLockConflictException(
                    "Department " + id + " was modified concurrently (expected version " + expectedVersion
                            + " but found " + department.getVersion() + ")");
        }

        departmentMapper.updateEntity(department, request);
        try {
            Department saved = departmentRepository.saveAndFlush(department);
            log.info("Updated department id={}", id);
            return departmentMapper.toResponse(saved);
        } catch (OptimisticLockException ex) {
            throw new OptimisticLockConflictException("Department " + id + " was modified concurrently. Please retry.");
        }
    }

    @Override
    public void delete(Long id) {
        Department department = findOrThrow(id);
        departmentRepository.delete(department);
        log.info("Deleted department id={}", id);
    }

    private Department findOrThrow(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
    }
}
