-- One payroll run per employee per month.
-- Indexing rationale:
--   idx_payroll_employee_month  unique guard against double-processing the
--                                same employee/month; also the natural lookup
--   idx_payroll_pay_month       "all payslips for month X" used by GET /payroll?month=
CREATE TABLE payrolls (
    id              BIGSERIAL PRIMARY KEY,
    employee_id     BIGINT        NOT NULL,
    pay_month       DATE          NOT NULL,
    basic_salary    NUMERIC(12,2) NOT NULL,
    deductions      NUMERIC(12,2) NOT NULL,
    net_salary      NUMERIC(12,2) NOT NULL,
    present_days    INT,
    absent_days     INT,
    status          VARCHAR(20)   NOT NULL,
    processed_at    TIMESTAMP,
    version         BIGINT        NOT NULL DEFAULT 0,
    created_at      TIMESTAMP     NOT NULL DEFAULT now(),
    updated_at      TIMESTAMP     NOT NULL DEFAULT now()
);

CREATE UNIQUE INDEX idx_payroll_employee_month ON payrolls (employee_id, pay_month);
CREATE INDEX idx_payroll_pay_month ON payrolls (pay_month);
