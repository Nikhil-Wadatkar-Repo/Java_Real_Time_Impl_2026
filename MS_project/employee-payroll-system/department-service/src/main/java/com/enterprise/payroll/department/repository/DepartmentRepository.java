package com.enterprise.payroll.department.repository;

import com.enterprise.payroll.department.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long>, JpaSpecificationExecutor<Department> {

    Optional<Department> findByName(String name);

    Optional<Department> findByCode(String code);

    boolean existsByName(String name);

    boolean existsByCode(String code);
}
