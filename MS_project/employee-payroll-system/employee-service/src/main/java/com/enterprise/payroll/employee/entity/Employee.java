package com.enterprise.payroll.employee.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Employee master record. {@code departmentId} is an id-only reference to
 * a row owned by department-service - no cross-service foreign key exists
 * because each service owns its own schema.
 * <p>
 * {@code version} backs Hibernate's optimistic locking so two concurrent
 * updates (e.g. HR updating salary while payroll reads it) do not silently
 * overwrite each other; the loser gets an {@link jakarta.persistence.OptimisticLockException}.
 */
@Entity
@Table(name = "employees", indexes = {
        @Index(name = "idx_employee_email", columnList = "email", unique = true),
        @Index(name = "idx_employee_department_id", columnList = "department_id"),
        @Index(name = "idx_employee_status", columnList = "status"),
        @Index(name = "idx_employee_salary", columnList = "salary"),
        @Index(name = "idx_employee_joining_date", columnList = "joining_date")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "department_id", nullable = false)
    private Long departmentId;

    @Column(name = "designation", length = 100)
    private String designation;

    @Column(name = "salary", nullable = false, precision = 12, scale = 2)
    private BigDecimal salary;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private EmployeeStatus status;

    @Column(name = "joining_date", nullable = false)
    private LocalDate joiningDate;

    /** Optimistic locking token - incremented by Hibernate on every update. */
    @Version
    @Column(name = "version")
    private Long version;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
