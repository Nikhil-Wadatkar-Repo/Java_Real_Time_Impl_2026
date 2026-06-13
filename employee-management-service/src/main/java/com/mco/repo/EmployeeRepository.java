package com.mco.repo;

import java.util.List;

import com.mco.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByEmail(String email);

    // @Query("SELECT e FROM Employee e JOIN FETCH e.department")
    // List<Employee> findAllWithDepartmentJoinFetch();

    // @Query("SELECT e FROM Employee e")
    // Page<Employee> findPagedEmployees(Pageable pageable);

    @Query("SELECT e FROM Employee e JOIN FETCH e.department")
    List<Employee> findAllWithDepartmentJoinFetch();

}
