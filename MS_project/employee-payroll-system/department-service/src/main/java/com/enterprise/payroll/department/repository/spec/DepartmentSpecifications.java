package com.enterprise.payroll.department.repository.spec;

import com.enterprise.payroll.department.dto.DepartmentSearchCriteria;
import com.enterprise.payroll.department.entity.Department;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.List;

/**
 * Builds a single {@link Specification} from {@link DepartmentSearchCriteria}.
 * Null candidates are filtered out explicitly rather than relying on any
 * null-tolerant combinator, since {@code Specification} composition
 * semantics around {@code null} have changed across Spring Data JPA
 * versions (see employee-service's equivalent class for the full rationale).
 */
public final class DepartmentSpecifications {

    private DepartmentSpecifications() {
    }

    public static Specification<Department> fromCriteria(DepartmentSearchCriteria criteria) {
        List<Specification<Department>> specs = Arrays.asList(
                nameContains(criteria.name()),
                codeContains(criteria.code()),
                locationContains(criteria.location())
        );

        Specification<Department> combined = (root, query, cb) -> cb.conjunction();
        for (Specification<Department> spec : specs) {
            if (spec != null) {
                combined = combined.and(spec);
            }
        }
        return combined;
    }

    private static Specification<Department> nameContains(String name) {
        if (name == null || name.isBlank()) {
            return null;
        }
        String pattern = "%" + name.toLowerCase() + "%";
        return (root, query, cb) -> cb.like(cb.lower(root.get("name")), pattern);
    }

    private static Specification<Department> codeContains(String code) {
        if (code == null || code.isBlank()) {
            return null;
        }
        String pattern = "%" + code.toLowerCase() + "%";
        return (root, query, cb) -> cb.like(cb.lower(root.get("code")), pattern);
    }

    private static Specification<Department> locationContains(String location) {
        if (location == null || location.isBlank()) {
            return null;
        }
        String pattern = "%" + location.toLowerCase() + "%";
        return (root, query, cb) -> cb.like(cb.lower(root.get("location")), pattern);
    }
}
