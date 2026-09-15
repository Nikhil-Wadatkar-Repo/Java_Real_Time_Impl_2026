package com.enterprise.payroll.project.controller;

import com.enterprise.payroll.project.dto.AssignmentResponse;
import com.enterprise.payroll.project.dto.ProjectRequest;
import com.enterprise.payroll.project.dto.ProjectResponse;
import com.enterprise.payroll.project.entity.ProjectStatus;
import com.enterprise.payroll.project.exception.DuplicateResourceException;
import com.enterprise.payroll.project.exception.InvalidRequestException;
import com.enterprise.payroll.project.exception.ResourceNotFoundException;
import com.enterprise.payroll.project.service.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProjectController.class)
@ActiveProfiles("test")
@org.springframework.context.annotation.Import(com.enterprise.payroll.project.exception.GlobalExceptionHandler.class)
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProjectService projectService;

    private ProjectRequest sampleRequest() {
        return new ProjectRequest("Platform Migration", "PLAT", "Migrate to new platform",
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 12, 31), ProjectStatus.ACTIVE);
    }

    private ProjectResponse sampleResponse() {
        return new ProjectResponse(1L, "Platform Migration", "PLAT", "Migrate to new platform",
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 12, 31), ProjectStatus.ACTIVE, 0L, null, null);
    }

    @Test
    void create_returns201_withLocationAndBody() throws Exception {
        when(projectService.create(any())).thenReturn(sampleResponse());

        mockMvc.perform(post("/projects")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(sampleRequest())))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/projects/1"))
                .andExpect(jsonPath("$.code").value("PLAT"));
    }

    @Test
    void create_returns400_whenValidationFails() throws Exception {
        ProjectRequest invalid = new ProjectRequest("", "", null, null, null, null);

        mockMvc.perform(post("/projects")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("VALIDATION_FAILED"));
    }

    @Test
    void create_returns409_whenDuplicateCode() throws Exception {
        when(projectService.create(any())).thenThrow(new DuplicateResourceException("duplicate"));

        mockMvc.perform(post("/projects")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(sampleRequest())))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("DUPLICATE_RESOURCE"));
    }

    @Test
    void getById_returns404_whenNotFound() throws Exception {
        when(projectService.getById(99L)).thenThrow(new ResourceNotFoundException("not found"));

        mockMvc.perform(get("/projects/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("RESOURCE_NOT_FOUND"));
    }

    @Test
    void getAll_returnsPagedResults() throws Exception {
        Page<ProjectResponse> page = new PageImpl<>(List.of(sampleResponse()), PageRequest.of(0, 20), 1);
        when(projectService.getAll(any())).thenReturn(page);

        mockMvc.perform(get("/projects?page=0&size=20&sort=startDate,desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].code").value("PLAT"));
    }

    @Test
    void assignEmployee_returns201() throws Exception {
        when(projectService.assignEmployee(1L, 42L))
                .thenReturn(new AssignmentResponse(1L, 42L, LocalDateTime.now()));

        mockMvc.perform(post("/projects/1/employees/42"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.employeeId").value(42));
    }

    @Test
    void assignEmployee_returns400_whenEmployeeNotFound() throws Exception {
        when(projectService.assignEmployee(anyLong(), anyLong()))
                .thenThrow(new InvalidRequestException("Employee not found with id: 42"));

        mockMvc.perform(post("/projects/1/employees/42"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("INVALID_REQUEST"));
    }

    @Test
    void removeEmployee_returns204() throws Exception {
        mockMvc.perform(delete("/projects/1/employees/42"))
                .andExpect(status().isNoContent());
    }

    @Test
    void removeEmployee_returns404_whenNotAssigned() throws Exception {
        org.mockito.Mockito.doThrow(new ResourceNotFoundException("not assigned"))
                .when(projectService).removeEmployee(1L, 42L);

        mockMvc.perform(delete("/projects/1/employees/42"))
                .andExpect(status().isNotFound());
    }
}
