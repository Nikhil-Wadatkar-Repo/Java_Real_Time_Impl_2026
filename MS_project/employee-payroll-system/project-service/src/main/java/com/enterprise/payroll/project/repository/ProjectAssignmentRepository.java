package com.enterprise.payroll.project.repository;

import com.enterprise.payroll.project.entity.ProjectAssignment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectAssignmentRepository extends JpaRepository<ProjectAssignment, Long> {

    boolean existsByProject_IdAndEmployeeId(Long projectId, Long employeeId);

    Optional<ProjectAssignment> findByProject_IdAndEmployeeId(Long projectId, Long employeeId);

    Page<ProjectAssignment> findByProject_Id(Long projectId, Pageable pageable);
}
