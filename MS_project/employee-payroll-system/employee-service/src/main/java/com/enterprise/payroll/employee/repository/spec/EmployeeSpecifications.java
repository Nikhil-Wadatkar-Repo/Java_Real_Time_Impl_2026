package com.enterprise.payroll.employee.repository.spec;

import com.enterprise.payroll.employee.dto.EmployeeSearchCriteria;
import com.enterprise.payroll.employee.entity.Employee;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.List;

/**
 * Builds a single {@link Specification} from {@link EmployeeSearchCriteria}.
 * Each filter is only added when the corresponding field is non-null, so a
 * request with no filters degenerates to "match everything" and the query
 * planner still uses the indexes declared on {@link Employee} for whichever
 * filters ARE present.
 * <p>
 * Individual {@code null} candidates are filtered out explicitly (rather
 * than relying on any null-tolerant combinator) since {@code Specification}
 * composition semantics around {@code null} have changed across Spring Data
 * JPA versions.
 */
public final class EmployeeSpecifications {

    private EmployeeSpecifications() {
    }

    public static Specification<Employee> fromCriteria(EmployeeSearchCriteria criteria) {
        List<Specification<Employee>> specs = Arrays.asList(
                nameContains(criteria.name()),
                hasDepartment(criteria.departmentId()),
                hasStatus(criteria.status()),
                salaryGreaterOrEqual(criteria.minSalary()),
                salaryLessOrEqual(criteria.maxSalary()),
                joinedAfter(criteria.joinedAfter()),
                joinedBefore(criteria.joinedBefore())
        );

        Specification<Employee> combined = (root, query, cb) -> cb.conjunction();
        for (Specification<Employee> spec : specs) {
            if (spec != null) {
                combined = combined.and(spec);
            }
        }
        return combined;
    }

    private static Specification<Employee> nameContains(String name) {
        if (name == null || name.isBlank()) {
            return null;
        }
        String pattern = "%" + name.toLowerCase() + "%";
        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("firstName")), pattern),
                cb.like(cb.lower(root.get("lastName")), pattern)
        );
    }

    private static Specification<Employee> hasDepartment(Long departmentId) {
        if (departmentId == null) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get("departmentId"), departmentId);
    }

    private static Specification<Employee> hasStatus(Object status) {
        if (status == null) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }

    private static Specification<Employee> salaryGreaterOrEqual(java.math.BigDecimal minSalary) {
        if (minSalary == null) {
            return null;
        }
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("salary"), minSalary);
    }

    private static Specification<Employee> salaryLessOrEqual(java.math.BigDecimal maxSalary) {
        if (maxSalary == null) {
            return null;
        }
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("salary"), maxSalary);
    }

    private static Specification<Employee> joinedAfter(java.time.LocalDate date) {
        if (date == null) {
            return null;
        }
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("joiningDate"), date);
    }

    private static Specification<Employee> joinedBefore(java.time.LocalDate date) {
        if (date == null) {
            return null;
        }
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("joiningDate"), date);
    }
}
