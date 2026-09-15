package com.enterprise.payroll.project.repository;

import com.enterprise.payroll.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long>, JpaSpecificationExecutor<Project> {

    Optional<Project> findByName(String name);

    Optional<Project> findByCode(String code);

    boolean existsByName(String name);

    boolean existsByCode(String code);
}
