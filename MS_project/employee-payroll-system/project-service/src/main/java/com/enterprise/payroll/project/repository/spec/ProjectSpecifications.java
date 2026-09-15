package com.enterprise.payroll.project.repository.spec;

import com.enterprise.payroll.project.dto.ProjectSearchCriteria;
import com.enterprise.payroll.project.entity.Project;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.List;

/**
 * Builds a single {@link Specification} from {@link ProjectSearchCriteria}.
 * Null candidates are filtered out explicitly rather than relying on any
 * null-tolerant combinator - see employee-service's equivalent class for
 * the full rationale (Specification composition semantics around
 * {@code null} have changed across Spring Data JPA versions).
 */
public final class ProjectSpecifications {

    private ProjectSpecifications() {
    }

    public static Specification<Project> fromCriteria(ProjectSearchCriteria criteria) {
        List<Specification<Project>> specs = Arrays.asList(
                nameContains(criteria.name()),
                codeContains(criteria.code()),
                hasStatus(criteria.status()),
                startedAfter(criteria.startedAfter()),
                startedBefore(criteria.startedBefore())
        );

        Specification<Project> combined = (root, query, cb) -> cb.conjunction();
        for (Specification<Project> spec : specs) {
            if (spec != null) {
                combined = combined.and(spec);
            }
        }
        return combined;
    }

    private static Specification<Project> nameContains(String name) {
        if (name == null || name.isBlank()) {
            return null;
        }
        String pattern = "%" + name.toLowerCase() + "%";
        return (root, query, cb) -> cb.like(cb.lower(root.get("name")), pattern);
    }

    private static Specification<Project> codeContains(String code) {
        if (code == null || code.isBlank()) {
            return null;
        }
        String pattern = "%" + code.toLowerCase() + "%";
        return (root, query, cb) -> cb.like(cb.lower(root.get("code")), pattern);
    }

    private static Specification<Project> hasStatus(Object status) {
        if (status == null) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }

    private static Specification<Project> startedAfter(java.time.LocalDate date) {
        if (date == null) {
            return null;
        }
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("startDate"), date);
    }

    private static Specification<Project> startedBefore(java.time.LocalDate date) {
        if (date == null) {
            return null;
        }
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("startDate"), date);
    }
}
