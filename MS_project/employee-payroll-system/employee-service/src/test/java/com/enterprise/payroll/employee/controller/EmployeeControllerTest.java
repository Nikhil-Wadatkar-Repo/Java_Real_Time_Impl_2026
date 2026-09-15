package com.enterprise.payroll.employee.controller;

import com.enterprise.payroll.employee.dto.EmployeeRequest;
import com.enterprise.payroll.employee.dto.EmployeeResponse;
import com.enterprise.payroll.employee.entity.EmployeeStatus;
import com.enterprise.payroll.employee.exception.DuplicateResourceException;
import com.enterprise.payroll.employee.exception.ResourceNotFoundException;
import com.enterprise.payroll.employee.service.EmployeeService;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = EmployeeController.class)
@ActiveProfiles("test")
@org.springframework.context.annotation.Import(com.enterprise.payroll.employee.exception.GlobalExceptionHandler.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EmployeeService employeeService;

    private EmployeeRequest sampleRequest() {
        return new EmployeeRequest("Grace", "Hopper", "grace@example.com", 1L,
                "Engineer", new BigDecimal("120000.00"), EmployeeStatus.ACTIVE, LocalDate.of(2020, 5, 1));
    }

    private EmployeeResponse sampleResponse() {
        return new EmployeeResponse(1L, "Grace", "Hopper", "grace@example.com", 1L,
                "Engineer", new BigDecimal("120000.00"), EmployeeStatus.ACTIVE,
                LocalDate.of(2020, 5, 1), 0L, null, null);
    }

    @Test
    void create_returns201_withLocationAndBody() throws Exception {
        when(employeeService.create(any())).thenReturn(sampleResponse());

        mockMvc.perform(post("/employees")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(sampleRequest())))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/employees/1"))
                .andExpect(jsonPath("$.email").value("grace@example.com"));
    }

    @Test
    void create_returns400_whenValidationFails() throws Exception {
        EmployeeRequest invalid = new EmployeeRequest("", "Hopper", "not-an-email", null,
                "Engineer", new BigDecimal("-5"), null, LocalDate.now().plusDays(1));

        mockMvc.perform(post("/employees")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("VALIDATION_FAILED"));
    }

    @Test
    void create_returns409_whenDuplicateEmail() throws Exception {
        when(employeeService.create(any())).thenThrow(new DuplicateResourceException("duplicate"));

        mockMvc.perform(post("/employees")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(sampleRequest())))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("DUPLICATE_RESOURCE"));
    }

    @Test
    void getById_returns404_whenNotFound() throws Exception {
        when(employeeService.getById(99L)).thenThrow(new ResourceNotFoundException("not found"));

        mockMvc.perform(get("/employees/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("RESOURCE_NOT_FOUND"));
    }

    @Test
    void getById_returns200_whenFound() throws Exception {
        when(employeeService.getById(1L)).thenReturn(sampleResponse());

        mockMvc.perform(get("/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Grace"));
    }

    @Test
    void getAll_returnsPagedResults() throws Exception {
        Page<EmployeeResponse> page = new PageImpl<>(List.of(sampleResponse()), PageRequest.of(0, 20), 1);
        when(employeeService.getAll(any())).thenReturn(page);

        mockMvc.perform(get("/employees?page=0&size=20&sort=salary,desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].email").value("grace@example.com"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void delete_returns204() throws Exception {
        mockMvc.perform(delete("/employees/1"))
                .andExpect(status().isNoContent());
    }
}
