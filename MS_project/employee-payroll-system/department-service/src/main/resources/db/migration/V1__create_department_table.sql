-- Department master table.
-- Indexing rationale:
--   idx_department_name  unique lookup + duplicate-check on create/update
--   idx_department_code  unique lookup by the short human-facing code used in reports/org charts
--   idx_department_location  "departments in location X" filter used by search
CREATE TABLE departments (
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(150)  NOT NULL,
    code            VARCHAR(20)   NOT NULL,
    description     VARCHAR(500),
    location        VARCHAR(150),
    version         BIGINT        NOT NULL DEFAULT 0,
    created_at      TIMESTAMP     NOT NULL DEFAULT now(),
    updated_at      TIMESTAMP     NOT NULL DEFAULT now()
);

CREATE UNIQUE INDEX idx_department_name ON departments (name);
CREATE UNIQUE INDEX idx_department_code ON departments (code);
CREATE INDEX idx_department_location ON departments (location);
