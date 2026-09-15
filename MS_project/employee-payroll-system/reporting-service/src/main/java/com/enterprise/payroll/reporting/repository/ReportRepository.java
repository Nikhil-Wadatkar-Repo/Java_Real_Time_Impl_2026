package com.enterprise.payroll.reporting.repository;

import com.enterprise.payroll.reporting.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
