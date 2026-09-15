package com.enterprise.payroll.payroll.repository;

import com.enterprise.payroll.payroll.entity.Payroll;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface PayrollRepository extends JpaRepository<Payroll, Long> {

    Optional<Payroll> findByEmployeeIdAndPayMonth(Long employeeId, LocalDate payMonth);

    Page<Payroll> findByEmployeeId(Long employeeId, Pageable pageable);

    Page<Payroll> findByPayMonth(LocalDate payMonth, Pageable pageable);

    boolean existsByEmployeeIdAndPayMonth(Long employeeId, LocalDate payMonth);
}
