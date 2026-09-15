package com.enterprise.payroll.department.repository;

import com.enterprise.payroll.department.dto.DepartmentSearchCriteria;
import com.enterprise.payroll.department.entity.Department;
import com.enterprise.payroll.department.repository.spec.DepartmentSpecifications;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class DepartmentRepositoryTest {

    @Autowired
    private DepartmentRepository departmentRepository;

    private Department department(String name, String code, String location) {
        return departmentRepository.save(Department.builder()
                .name(name).code(code).description("desc").location(location)
                .build());
    }

    @Test
    void existsByNameAndCode_detectDuplicates() {
        department("Engineering", "ENG", "Bengaluru");

        assertThat(departmentRepository.existsByName("Engineering")).isTrue();
        assertThat(departmentRepository.existsByCode("ENG")).isTrue();
        assertThat(departmentRepository.existsByName("Missing")).isFalse();
    }

    @Test
    void search_filtersByNameCaseInsensitive() {
        department("Engineering", "ENG", "Bengaluru");
        department("Finance", "FIN", "Mumbai");

        DepartmentSearchCriteria criteria = new DepartmentSearchCriteria("engineer", null, null);

        Page<Department> result = departmentRepository.findAll(
                DepartmentSpecifications.fromCriteria(criteria), PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getCode()).isEqualTo("ENG");
    }

    @Test
    void search_filtersByLocation() {
        department("Engineering", "ENG", "Bengaluru");
        department("Finance", "FIN", "Mumbai");

        DepartmentSearchCriteria criteria = new DepartmentSearchCriteria(null, null, "mumbai");

        Page<Department> result = departmentRepository.findAll(
                DepartmentSpecifications.fromCriteria(criteria), PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getCode()).isEqualTo("FIN");
    }
}
