package com.enterprise.payroll.employee.repository;

import com.enterprise.payroll.employee.dto.EmployeeSearchCriteria;
import com.enterprise.payroll.employee.entity.Employee;
import com.enterprise.payroll.employee.entity.EmployeeStatus;
import com.enterprise.payroll.employee.repository.spec.EmployeeSpecifications;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee(String first, String last, String email, Long deptId,
                               BigDecimal salary, EmployeeStatus status, LocalDate joiningDate) {
        return employeeRepository.save(Employee.builder()
                .firstName(first).lastName(last).email(email)
                .departmentId(deptId).designation("Engineer")
                .salary(salary).status(status).joiningDate(joiningDate)
                .build());
    }

    @Test
    void existsByEmail_detectsDuplicates() {
        employee("Ada", "Lovelace", "ada@example.com", 1L,
                new BigDecimal("90000"), EmployeeStatus.ACTIVE, LocalDate.of(2021, 3, 1));

        assertThat(employeeRepository.existsByEmail("ada@example.com")).isTrue();
        assertThat(employeeRepository.existsByEmail("missing@example.com")).isFalse();
    }

    @Test
    void search_filtersByDepartmentStatusAndSalaryRange() {
        employee("Ada", "Lovelace", "ada@example.com", 1L,
                new BigDecimal("90000"), EmployeeStatus.ACTIVE, LocalDate.of(2021, 3, 1));
        employee("Grace", "Hopper", "grace@example.com", 1L,
                new BigDecimal("150000"), EmployeeStatus.ACTIVE, LocalDate.of(2019, 6, 15));
        employee("Alan", "Turing", "alan@example.com", 2L,
                new BigDecimal("110000"), EmployeeStatus.TERMINATED, LocalDate.of(2018, 1, 1));

        EmployeeSearchCriteria criteria = new EmployeeSearchCriteria(
                null, 1L, EmployeeStatus.ACTIVE, new BigDecimal("50000"), new BigDecimal("100000"), null, null);

        Page<Employee> result = employeeRepository.findAll(
                EmployeeSpecifications.fromCriteria(criteria), PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getEmail()).isEqualTo("ada@example.com");
    }

    @Test
    void search_filtersByNameCaseInsensitive() {
        employee("Ada", "Lovelace", "ada@example.com", 1L,
                new BigDecimal("90000"), EmployeeStatus.ACTIVE, LocalDate.of(2021, 3, 1));

        EmployeeSearchCriteria criteria = new EmployeeSearchCriteria(
                "lovelace", null, null, null, null, null, null);

        Page<Employee> result = employeeRepository.findAll(
                EmployeeSpecifications.fromCriteria(criteria), PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(1);
    }
}
