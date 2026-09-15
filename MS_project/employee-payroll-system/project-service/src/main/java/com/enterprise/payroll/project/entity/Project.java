package com.enterprise.payroll.project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Project master record. Employee assignments live in a separate
 * {@link ProjectAssignment} table (a real FK to {@code Project} since both
 * live in this service's own schema) that only stores the assigned
 * employee's id - never a join to employee-service's data.
 */
@Entity
@Table(name = "projects", indexes = {
        @Index(name = "idx_project_name", columnList = "name", unique = true),
        @Index(name = "idx_project_code", columnList = "code", unique = true),
        @Index(name = "idx_project_status", columnList = "status"),
        @Index(name = "idx_project_start_date", columnList = "start_date")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 150)
    private String name;

    @Column(name = "code", nullable = false, unique = true, length = 20)
    private String code;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ProjectStatus status;

    /** Optimistic locking token - guards against lost updates on concurrent edits. */
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
