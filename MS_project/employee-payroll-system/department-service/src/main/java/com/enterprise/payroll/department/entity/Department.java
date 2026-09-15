package com.enterprise.payroll.department.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Department master record. {@code code} is the short human-facing
 * identifier (e.g. "ENG", "HR") used in reports and org charts; {@code name}
 * is the full display name. Both are unique - a duplicate of either is
 * treated as the same conflict from the client's point of view.
 */
@Entity
@Table(name = "departments", indexes = {
        @Index(name = "idx_department_name", columnList = "name", unique = true),
        @Index(name = "idx_department_code", columnList = "code", unique = true),
        @Index(name = "idx_department_location", columnList = "location")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 150)
    private String name;

    @Column(name = "code", nullable = false, unique = true, length = 20)
    private String code;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "location", length = 150)
    private String location;

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
