package com.enterprise.payroll.employee.service;

import com.enterprise.payroll.employee.client.DepartmentClient;
import com.enterprise.payroll.employee.dto.EmployeeRequest;
import com.enterprise.payroll.employee.dto.EmployeeResponse;
import com.enterprise.payroll.employee.entity.Employee;
import com.enterprise.payroll.employee.entity.EmployeeStatus;
import com.enterprise.payroll.employee.exception.DuplicateResourceException;
import com.enterprise.payroll.employee.exception.ExternalServiceException;
import com.enterprise.payroll.employee.exception.InvalidRequestException;
import com.enterprise.payroll.employee.exception.OptimisticLockConflictException;
import com.enterprise.payroll.employee.exception.ResourceNotFoundException;
import com.enterprise.payroll.employee.mapper.EmployeeMapper;
import com.enterprise.payroll.employee.repository.EmployeeRepository;
import com.enterprise.payroll.employee.service.impl.EmployeeServiceImpl;
import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import feign.Response;
import feign.RetryableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentClient departmentClient;

    private final EmployeeMapper employeeMapper = new EmployeeMapper();

    private EmployeeServiceImpl employeeService;

    private EmployeeRequest request;
    private Employee entity;

    @BeforeEach
    void setUp() {
        // The mapper is a cheap, pure, real instance - no reason to mock it.
        employeeService = new EmployeeServiceImpl(employeeRepository, employeeMapper, departmentClient);

        request = new EmployeeRequest("Ada", "Lovelace", "ada@example.com", 1L,
                "Engineer", new BigDecimal("95000.00"), EmployeeStatus.ACTIVE, LocalDate.of(2022, 1, 10));

        entity = Employee.builder()
                .id(1L)
                .firstName("Ada").lastName("Lovelace").email("ada@example.com")
                .departmentId(1L).designation("Engineer")
                .salary(new BigDecimal("95000.00")).status(EmployeeStatus.ACTIVE)
                .joiningDate(LocalDate.of(2022, 1, 10)).version(0L)
                .build();
    }

    private FeignException.NotFound departmentNotFoundException() {
        Request feignRequest = Request.create(Request.HttpMethod.GET, "/departments/1",
                Collections.emptyMap(), null, java.nio.charset.StandardCharsets.UTF_8, new RequestTemplate());
        Response response = Response.builder()
                .status(404)
                .reason("Not Found")
                .request(feignRequest)
                .headers(Collections.emptyMap())
                .build();
        return (FeignException.NotFound) FeignException.errorStatus("DepartmentClient#getDepartmentById", response);
    }

    @Test
    void create_savesEmployee_whenEmailNotTaken() {
        when(employeeRepository.existsByEmail("ada@example.com")).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenReturn(entity);

        EmployeeResponse response = employeeService.create(request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.email()).isEqualTo("ada@example.com");
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void create_throwsDuplicate_whenEmailAlreadyExists() {
        when(employeeRepository.existsByEmail("ada@example.com")).thenReturn(true);

        assertThatThrownBy(() -> employeeService.create(request))
                .isInstanceOf(DuplicateResourceException.class);
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void getById_throwsNotFound_whenMissing() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.getById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getById_returnsEmployee_whenPresent() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(entity));

        EmployeeResponse response = employeeService.getById(1L);

        assertThat(response.firstName()).isEqualTo("Ada");
    }

    @Test
    void update_throwsOptimisticLockConflict_whenVersionMismatch() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(employeeRepository.findByEmail("ada@example.com")).thenReturn(Optional.of(entity));

        assertThatThrownBy(() -> employeeService.update(1L, request, 5L))
                .isInstanceOf(OptimisticLockConflictException.class);
        verify(employeeRepository, never()).saveAndFlush(any());
    }

    @Test
    void update_succeeds_whenVersionMatches() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(employeeRepository.findByEmail("ada@example.com")).thenReturn(Optional.of(entity));
        when(employeeRepository.saveAndFlush(any(Employee.class))).thenReturn(entity);

        EmployeeResponse response = employeeService.update(1L, request, 0L);

        assertThat(response.id()).isEqualTo(1L);
    }

    @Test
    void delete_removesEmployee_whenExists() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(entity));

        employeeService.delete(1L);

        verify(employeeRepository).delete(entity);
    }

    @Test
    void create_throwsInvalidRequest_whenDepartmentDoesNotExist() {
        when(departmentClient.getDepartmentById(1L)).thenThrow(departmentNotFoundException());

        assertThatThrownBy(() -> employeeService.create(request))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessageContaining("Department not found");
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void create_throwsExternalServiceException_whenDepartmentServiceUnreachable() {
        RetryableException connectionRefused = new RetryableException(
                -1, "Connection refused", Request.HttpMethod.GET, (Long) null,
                Request.create(Request.HttpMethod.GET, "/departments/1", Collections.emptyMap(),
                        null, new RequestTemplate()));
        when(departmentClient.getDepartmentById(1L)).thenThrow(connectionRefused);

        assertThatThrownBy(() -> employeeService.create(request))
                .isInstanceOf(ExternalServiceException.class);
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void update_throwsInvalidRequest_whenDepartmentDoesNotExist() {
        when(departmentClient.getDepartmentById(1L)).thenThrow(departmentNotFoundException());

        assertThatThrownBy(() -> employeeService.update(1L, request, 0L))
                .isInstanceOf(InvalidRequestException.class);
        verify(employeeRepository, never()).findById(any());
    }
}
