package com.mco.repo;
import com.mco.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
public interface AuditRepository extends JpaRepository<AuditLog, Long> {}