-- One row per employee per calendar day.
-- Indexing rationale:
--   idx_attendance_employee_date  unique guard against double check-in the same day;
--                                  also the natural lookup used by check-in/check-out
--   idx_attendance_work_date      date-range queries (monthly summaries, payroll processing)
CREATE TABLE attendance (
    id              BIGSERIAL PRIMARY KEY,
    employee_id     BIGINT      NOT NULL,
    work_date       DATE        NOT NULL,
    check_in_time   TIMESTAMP,
    check_out_time  TIMESTAMP,
    working_hours   NUMERIC(5,2),
    status          VARCHAR(20) NOT NULL,
    version         BIGINT      NOT NULL DEFAULT 0,
    created_at      TIMESTAMP   NOT NULL DEFAULT now(),
    updated_at      TIMESTAMP   NOT NULL DEFAULT now()
);

CREATE UNIQUE INDEX idx_attendance_employee_date ON attendance (employee_id, work_date);
CREATE INDEX idx_attendance_work_date ON attendance (work_date);
