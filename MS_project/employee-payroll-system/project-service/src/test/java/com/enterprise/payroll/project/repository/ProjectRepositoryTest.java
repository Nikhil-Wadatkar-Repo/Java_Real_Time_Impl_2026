package com.enterprise.payroll.project.repository;

import com.enterprise.payroll.project.dto.ProjectSearchCriteria;
import com.enterprise.payroll.project.entity.Project;
import com.enterprise.payroll.project.entity.ProjectAssignment;
import com.enterprise.payroll.project.entity.ProjectStatus;
import com.enterprise.payroll.project.repository.spec.ProjectSpecifications;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ProjectRepositoryTest {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectAssignmentRepository projectAssignmentRepository;

    private Project project(String name, String code, ProjectStatus status, LocalDate startDate) {
        return projectRepository.save(Project.builder()
                .name(name).code(code).description("desc")
                .startDate(startDate).status(status)
                .build());
    }

    @Test
    void existsByNameAndCode_detectDuplicates() {
        project("Platform Migration", "PLAT", ProjectStatus.ACTIVE, LocalDate.of(2026, 1, 1));

        assertThat(projectRepository.existsByName("Platform Migration")).isTrue();
        assertThat(projectRepository.existsByCode("PLAT")).isTrue();
        assertThat(projectRepository.existsByName("Missing")).isFalse();
    }

    @Test
    void search_filtersByStatusAndStartDateRange() {
        project("Platform Migration", "PLAT", ProjectStatus.ACTIVE, LocalDate.of(2026, 1, 1));
        project("Legacy Cleanup", "LEG", ProjectStatus.COMPLETED, LocalDate.of(2020, 1, 1));

        ProjectSearchCriteria criteria = new ProjectSearchCriteria(
                null, null, ProjectStatus.ACTIVE, LocalDate.of(2025, 1, 1), LocalDate.of(2027, 1, 1));

        Page<Project> result = projectRepository.findAll(
                ProjectSpecifications.fromCriteria(criteria), PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getCode()).isEqualTo("PLAT");
    }

    @Test
    void assignment_uniqueConstraint_preventsDuplicateAssignment() {
        Project saved = project("Platform Migration", "PLAT", ProjectStatus.ACTIVE, LocalDate.of(2026, 1, 1));

        projectAssignmentRepository.save(ProjectAssignment.builder().project(saved).employeeId(42L).build());

        assertThat(projectAssignmentRepository.existsByProject_IdAndEmployeeId(saved.getId(), 42L)).isTrue();
        assertThat(projectAssignmentRepository.existsByProject_IdAndEmployeeId(saved.getId(), 99L)).isFalse();
    }
}
