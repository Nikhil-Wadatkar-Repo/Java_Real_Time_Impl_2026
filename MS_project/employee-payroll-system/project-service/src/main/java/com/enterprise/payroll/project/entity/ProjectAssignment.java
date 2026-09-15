package com.enterprise.payroll.project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Join record between a {@link Project} (real FK, same schema) and an
 * employee (id-only reference - employee-service owns that data). The
 * unique constraint on {@code (project_id, employee_id)} is what actually
 * prevents assigning the same employee twice, backed by
 * {@code idx_project_assignment_unique}.
 */
@Entity
@Table(name = "project_assignments", indexes = {
        @Index(name = "idx_project_assignment_unique", columnList = "project_id, employee_id", unique = true),
        @Index(name = "idx_project_assignment_employee_id", columnList = "employee_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @CreationTimestamp
    @Column(name = "assigned_at", updatable = false)
    private LocalDateTime assignedAt;
}
