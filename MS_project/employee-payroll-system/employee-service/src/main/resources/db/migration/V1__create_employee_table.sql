-- Employee master table.
-- Indexing rationale:
--   idx_employee_email          unique lookup for login/identity + duplicate-check on create/update
--   idx_employee_department_id  every "employees in department X" query (Project/Reporting call this a lot)
--   idx_employee_status         payroll processing filters ACTIVE/ON_LEAVE employees each run
--   idx_employee_salary         salary range search + sort=salary used by GET /employees
--   idx_employee_joining_date   joining-date range filters in search, and seniority/report queries
CREATE TABLE employees (
    id              BIGSERIAL PRIMARY KEY,
    first_name      VARCHAR(100)   NOT NULL,
    last_name       VARCHAR(100)   NOT NULL,
    email           VARCHAR(150)   NOT NULL,
    department_id   BIGINT         NOT NULL,
    designation     VARCHAR(100),
    salary          NUMERIC(12,2)  NOT NULL,
    status          VARCHAR(20)    NOT NULL,
    joining_date    DATE           NOT NULL,
    version         BIGINT         NOT NULL DEFAULT 0,
    created_at      TIMESTAMP      NOT NULL DEFAULT now(),
    updated_at      TIMESTAMP      NOT NULL DEFAULT now()
);

CREATE UNIQUE INDEX idx_employee_email ON employees (email);
CREATE INDEX idx_employee_department_id ON employees (department_id);
CREATE INDEX idx_employee_status ON employees (status);
CREATE INDEX idx_employee_salary ON employees (salary);
CREATE INDEX idx_employee_joining_date ON employees (joining_date);
