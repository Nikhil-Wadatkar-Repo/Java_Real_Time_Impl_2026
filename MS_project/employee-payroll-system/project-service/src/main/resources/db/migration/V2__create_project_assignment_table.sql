-- Employee-to-project assignment. employee_id is an id-only reference into
-- employee-service's own database - no cross-service foreign key exists.
-- Indexing rationale:
--   idx_project_assignment_unique       the real guard against double-assigning
--                                        the same employee to the same project;
--                                        also the natural "is X on project Y" lookup
--   idx_project_assignment_employee_id  reverse lookup "which projects is employee X on"
--                                        (useful for payroll/reporting later)
CREATE TABLE project_assignments (
    id              BIGSERIAL PRIMARY KEY,
    project_id      BIGINT      NOT NULL REFERENCES projects (id),
    employee_id     BIGINT      NOT NULL,
    assigned_at     TIMESTAMP   NOT NULL DEFAULT now()
);

CREATE UNIQUE INDEX idx_project_assignment_unique ON project_assignments (project_id, employee_id);
CREATE INDEX idx_project_assignment_employee_id ON project_assignments (employee_id);
