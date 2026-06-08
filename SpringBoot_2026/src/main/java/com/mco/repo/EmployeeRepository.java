package com.mco.repo;

import java.util.List;

import com.mco.entity.Employee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByEmail(String email);

    @Query("select e from Employee e join fetch e.department")
    List<Employee> findAllWithDepartmentJoinFetch();
}
