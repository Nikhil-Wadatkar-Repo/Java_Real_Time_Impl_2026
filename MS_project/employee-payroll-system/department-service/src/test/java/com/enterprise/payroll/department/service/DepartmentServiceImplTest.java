package com.enterprise.payroll.department.service;

import com.enterprise.payroll.department.dto.DepartmentRequest;
import com.enterprise.payroll.department.dto.DepartmentResponse;
import com.enterprise.payroll.department.entity.Department;
import com.enterprise.payroll.department.exception.DuplicateResourceException;
import com.enterprise.payroll.department.exception.OptimisticLockConflictException;
import com.enterprise.payroll.department.exception.ResourceNotFoundException;
import com.enterprise.payroll.department.mapper.DepartmentMapper;
import com.enterprise.payroll.department.repository.DepartmentRepository;
import com.enterprise.payroll.department.service.impl.DepartmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceImplTest {

    @Mock
    private DepartmentRepository departmentRepository;

    private final DepartmentMapper departmentMapper = new DepartmentMapper();

    private DepartmentServiceImpl departmentService;

    private DepartmentRequest request;
    private Department entity;

    @BeforeEach
    void setUp() {
        // The mapper is a cheap, pure, real instance - no reason to mock it.
        departmentService = new DepartmentServiceImpl(departmentRepository, departmentMapper);

        request = new DepartmentRequest("Engineering", "ENG", "Builds the product", "Bengaluru");

        entity = Department.builder()
                .id(1L).name("Engineering").code("ENG")
                .description("Builds the product").location("Bengaluru")
                .version(0L)
                .build();
    }

    @Test
    void create_savesDepartment_whenNameAndCodeNotTaken() {
        when(departmentRepository.existsByName("Engineering")).thenReturn(false);
        when(departmentRepository.existsByCode("ENG")).thenReturn(false);
        when(departmentRepository.save(any(Department.class))).thenReturn(entity);

        DepartmentResponse response = departmentService.create(request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.code()).isEqualTo("ENG");
        verify(departmentRepository).save(any(Department.class));
    }

    @Test
    void create_throwsDuplicate_whenNameAlreadyExists() {
        when(departmentRepository.existsByName("Engineering")).thenReturn(true);

        assertThatThrownBy(() -> departmentService.create(request))
                .isInstanceOf(DuplicateResourceException.class);
        verify(departmentRepository, never()).save(any());
    }

    @Test
    void create_throwsDuplicate_whenCodeAlreadyExists() {
        when(departmentRepository.existsByName("Engineering")).thenReturn(false);
        when(departmentRepository.existsByCode("ENG")).thenReturn(true);

        assertThatThrownBy(() -> departmentService.create(request))
                .isInstanceOf(DuplicateResourceException.class);
        verify(departmentRepository, never()).save(any());
    }

    @Test
    void getById_throwsNotFound_whenMissing() {
        when(departmentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> departmentService.getById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getById_returnsDepartment_whenPresent() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(entity));

        DepartmentResponse response = departmentService.getById(1L);

        assertThat(response.name()).isEqualTo("Engineering");
    }

    @Test
    void update_throwsOptimisticLockConflict_whenVersionMismatch() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(departmentRepository.findByName("Engineering")).thenReturn(Optional.of(entity));
        when(departmentRepository.findByCode("ENG")).thenReturn(Optional.of(entity));

        assertThatThrownBy(() -> departmentService.update(1L, request, 5L))
                .isInstanceOf(OptimisticLockConflictException.class);
        verify(departmentRepository, never()).saveAndFlush(any());
    }

    @Test
    void update_succeeds_whenVersionMatches() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(departmentRepository.findByName("Engineering")).thenReturn(Optional.of(entity));
        when(departmentRepository.findByCode("ENG")).thenReturn(Optional.of(entity));
        when(departmentRepository.saveAndFlush(any(Department.class))).thenReturn(entity);

        DepartmentResponse response = departmentService.update(1L, request, 0L);

        assertThat(response.id()).isEqualTo(1L);
    }

    @Test
    void delete_removesDepartment_whenExists() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(entity));

        departmentService.delete(1L);

        verify(departmentRepository).delete(entity);
    }
}
