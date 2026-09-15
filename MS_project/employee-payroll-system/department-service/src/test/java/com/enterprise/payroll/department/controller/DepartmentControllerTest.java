package com.enterprise.payroll.department.controller;

import com.enterprise.payroll.department.dto.DepartmentRequest;
import com.enterprise.payroll.department.dto.DepartmentResponse;
import com.enterprise.payroll.department.exception.DuplicateResourceException;
import com.enterprise.payroll.department.exception.ResourceNotFoundException;
import com.enterprise.payroll.department.service.DepartmentService;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = DepartmentController.class)
@ActiveProfiles("test")
@org.springframework.context.annotation.Import(com.enterprise.payroll.department.exception.GlobalExceptionHandler.class)
class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DepartmentService departmentService;

    private DepartmentRequest sampleRequest() {
        return new DepartmentRequest("Engineering", "ENG", "Builds the product", "Bengaluru");
    }

    private DepartmentResponse sampleResponse() {
        return new DepartmentResponse(1L, "Engineering", "ENG", "Builds the product", "Bengaluru", 0L, null, null);
    }

    @Test
    void create_returns201_withLocationAndBody() throws Exception {
        when(departmentService.create(any())).thenReturn(sampleResponse());

        mockMvc.perform(post("/departments")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(sampleRequest())))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/departments/1"))
                .andExpect(jsonPath("$.code").value("ENG"));
    }

    @Test
    void create_returns400_whenValidationFails() throws Exception {
        DepartmentRequest invalid = new DepartmentRequest("", "", null, null);

        mockMvc.perform(post("/departments")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("VALIDATION_FAILED"));
    }

    @Test
    void create_returns409_whenDuplicateCode() throws Exception {
        when(departmentService.create(any())).thenThrow(new DuplicateResourceException("duplicate"));

        mockMvc.perform(post("/departments")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(sampleRequest())))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("DUPLICATE_RESOURCE"));
    }

    @Test
    void getById_returns404_whenNotFound() throws Exception {
        when(departmentService.getById(99L)).thenThrow(new ResourceNotFoundException("not found"));

        mockMvc.perform(get("/departments/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("RESOURCE_NOT_FOUND"));
    }

    @Test
    void getById_returns200_whenFound() throws Exception {
        when(departmentService.getById(1L)).thenReturn(sampleResponse());

        mockMvc.perform(get("/departments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Engineering"));
    }

    @Test
    void getAll_returnsPagedResults() throws Exception {
        Page<DepartmentResponse> page = new PageImpl<>(List.of(sampleResponse()), PageRequest.of(0, 20), 1);
        when(departmentService.getAll(any())).thenReturn(page);

        mockMvc.perform(get("/departments?page=0&size=20&sort=name,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].code").value("ENG"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void delete_returns204() throws Exception {
        mockMvc.perform(delete("/departments/1"))
                .andExpect(status().isNoContent());
    }
}
