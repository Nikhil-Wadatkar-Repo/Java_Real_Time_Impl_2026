package com.enterprise.payroll.project.service;

import com.enterprise.payroll.project.client.EmployeeClient;
import com.enterprise.payroll.project.dto.AssignmentResponse;
import com.enterprise.payroll.project.dto.ProjectRequest;
import com.enterprise.payroll.project.dto.ProjectResponse;
import com.enterprise.payroll.project.entity.Project;
import com.enterprise.payroll.project.entity.ProjectAssignment;
import com.enterprise.payroll.project.entity.ProjectStatus;
import com.enterprise.payroll.project.exception.DuplicateResourceException;
import com.enterprise.payroll.project.exception.ExternalServiceException;
import com.enterprise.payroll.project.exception.InvalidRequestException;
import com.enterprise.payroll.project.exception.OptimisticLockConflictException;
import com.enterprise.payroll.project.exception.ResourceNotFoundException;
import com.enterprise.payroll.project.mapper.ProjectMapper;
import com.enterprise.payroll.project.repository.ProjectAssignmentRepository;
import com.enterprise.payroll.project.repository.ProjectRepository;
import com.enterprise.payroll.project.service.impl.ProjectServiceImpl;
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

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceImplTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectAssignmentRepository projectAssignmentRepository;

    @Mock
    private EmployeeClient employeeClient;

    private final ProjectMapper projectMapper = new ProjectMapper();

    private ProjectServiceImpl projectService;

    private ProjectRequest request;
    private Project entity;

    @BeforeEach
    void setUp() {
        projectService = new ProjectServiceImpl(
                projectRepository, projectAssignmentRepository, projectMapper, employeeClient);

        request = new ProjectRequest("Platform Migration", "PLAT", "Migrate to new platform",
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 12, 31), ProjectStatus.ACTIVE);

        entity = Project.builder()
                .id(1L).name("Platform Migration").code("PLAT")
                .description("Migrate to new platform")
                .startDate(LocalDate.of(2026, 1, 1)).endDate(LocalDate.of(2026, 12, 31))
                .status(ProjectStatus.ACTIVE).version(0L)
                .build();
    }

    private FeignException.NotFound employeeNotFoundException() {
        Request feignRequest = Request.create(Request.HttpMethod.GET, "/employees/1",
                Collections.emptyMap(), null, StandardCharsets.UTF_8, new RequestTemplate());
        Response response = Response.builder()
                .status(404).reason("Not Found").request(feignRequest)
                .headers(Collections.emptyMap()).build();
        return (FeignException.NotFound) FeignException.errorStatus("EmployeeClient#getEmployeeById", response);
    }

    @Test
    void create_savesProject_whenNameAndCodeNotTaken() {
        when(projectRepository.existsByName("Platform Migration")).thenReturn(false);
        when(projectRepository.existsByCode("PLAT")).thenReturn(false);
        when(projectRepository.save(any(Project.class))).thenReturn(entity);

        ProjectResponse response = projectService.create(request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.code()).isEqualTo("PLAT");
    }

    @Test
    void create_throwsInvalidRequest_whenEndDateBeforeStartDate() {
        ProjectRequest invalid = new ProjectRequest("X", "X1", null,
                LocalDate.of(2026, 6, 1), LocalDate.of(2026, 1, 1), ProjectStatus.PLANNED);

        assertThatThrownBy(() -> projectService.create(invalid))
                .isInstanceOf(InvalidRequestException.class);
        verify(projectRepository, never()).save(any());
    }

    @Test
    void create_throwsDuplicate_whenNameAlreadyExists() {
        when(projectRepository.existsByName("Platform Migration")).thenReturn(true);

        assertThatThrownBy(() -> projectService.create(request))
                .isInstanceOf(DuplicateResourceException.class);
        verify(projectRepository, never()).save(any());
    }

    @Test
    void create_throwsDuplicate_whenCodeAlreadyExists() {
        when(projectRepository.existsByName("Platform Migration")).thenReturn(false);
        when(projectRepository.existsByCode("PLAT")).thenReturn(true);

        assertThatThrownBy(() -> projectService.create(request))
                .isInstanceOf(DuplicateResourceException.class);
    }

    @Test
    void getById_throwsNotFound_whenMissing() {
        when(projectRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> projectService.getById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void update_throwsOptimisticLockConflict_whenVersionMismatch() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(projectRepository.findByName(any())).thenReturn(Optional.of(entity));
        when(projectRepository.findByCode(any())).thenReturn(Optional.of(entity));

        assertThatThrownBy(() -> projectService.update(1L, request, 5L))
                .isInstanceOf(OptimisticLockConflictException.class);
        verify(projectRepository, never()).saveAndFlush(any());
    }

    @Test
    void update_succeeds_whenVersionMatches() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(projectRepository.findByName(any())).thenReturn(Optional.of(entity));
        when(projectRepository.findByCode(any())).thenReturn(Optional.of(entity));
        when(projectRepository.saveAndFlush(any(Project.class))).thenReturn(entity);

        ProjectResponse response = projectService.update(1L, request, 0L);

        assertThat(response.id()).isEqualTo(1L);
    }

    @Test
    void delete_removesProject_whenExists() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(entity));

        projectService.delete(1L);

        verify(projectRepository).delete(entity);
    }

    @Test
    void assignEmployee_succeeds_whenEmployeeExistsAndNotAlreadyAssigned() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(projectAssignmentRepository.existsByProject_IdAndEmployeeId(1L, 42L)).thenReturn(false);
        ProjectAssignment saved = ProjectAssignment.builder().id(1L).project(entity).employeeId(42L).build();
        when(projectAssignmentRepository.save(any(ProjectAssignment.class))).thenReturn(saved);

        AssignmentResponse response = projectService.assignEmployee(1L, 42L);

        assertThat(response.employeeId()).isEqualTo(42L);
        assertThat(response.projectId()).isEqualTo(1L);
    }

    @Test
    void assignEmployee_throwsInvalidRequest_whenEmployeeDoesNotExist() {
        when(employeeClient.getEmployeeById(42L)).thenThrow(employeeNotFoundException());

        assertThatThrownBy(() -> projectService.assignEmployee(1L, 42L))
                .isInstanceOf(InvalidRequestException.class);
        verify(projectAssignmentRepository, never()).save(any());
    }

    @Test
    void assignEmployee_throwsExternalServiceException_whenEmployeeServiceUnreachable() {
        RetryableException connectionRefused = new RetryableException(
                -1, "Connection refused", Request.HttpMethod.GET, (Long) null,
                Request.create(Request.HttpMethod.GET, "/employees/42", Collections.emptyMap(),
                        null, StandardCharsets.UTF_8, new RequestTemplate()));
        when(employeeClient.getEmployeeById(42L)).thenThrow(connectionRefused);

        assertThatThrownBy(() -> projectService.assignEmployee(1L, 42L))
                .isInstanceOf(ExternalServiceException.class);
        verify(projectAssignmentRepository, never()).save(any());
    }

    @Test
    void assignEmployee_throwsDuplicate_whenAlreadyAssigned() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(projectAssignmentRepository.existsByProject_IdAndEmployeeId(1L, 42L)).thenReturn(true);

        assertThatThrownBy(() -> projectService.assignEmployee(1L, 42L))
                .isInstanceOf(DuplicateResourceException.class);
        verify(projectAssignmentRepository, never()).save(any());
    }

    @Test
    void removeEmployee_throwsNotFound_whenNotAssigned() {
        when(projectAssignmentRepository.findByProject_IdAndEmployeeId(1L, 42L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> projectService.removeEmployee(1L, 42L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void removeEmployee_removesAssignment_whenExists() {
        ProjectAssignment assignment = ProjectAssignment.builder().id(1L).project(entity).employeeId(42L).build();
        when(projectAssignmentRepository.findByProject_IdAndEmployeeId(1L, 42L)).thenReturn(Optional.of(assignment));

        projectService.removeEmployee(1L, 42L);

        verify(projectAssignmentRepository).delete(assignment);
    }
}
