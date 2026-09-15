package com.enterprise.payroll.employee.repository;

import com.enterprise.payroll.employee.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * {@link JpaSpecificationExecutor} lets the search endpoint compose
 * predicates dynamically instead of writing one query method per filter
 * combination (which would explode combinatorially).
 */
public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

    Optional<Employee> findByEmail(String email);

    boolean existsByEmail(String email);
}
