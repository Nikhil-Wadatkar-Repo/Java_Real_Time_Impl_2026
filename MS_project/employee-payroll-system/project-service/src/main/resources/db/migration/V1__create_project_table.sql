-- Project master table.
-- Indexing rationale:
--   idx_project_name        unique lookup + duplicate-check on create/update
--   idx_project_code        unique lookup by the short human-facing code used in reports
--   idx_project_status      "active projects" filter used constantly by search/reporting
--   idx_project_start_date  date-range search (e.g. "projects started this quarter")
CREATE TABLE projects (
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(150)  NOT NULL,
    code            VARCHAR(20)   NOT NULL,
    description     VARCHAR(500),
    start_date      DATE          NOT NULL,
    end_date        DATE,
    status          VARCHAR(20)   NOT NULL,
    version         BIGINT        NOT NULL DEFAULT 0,
    created_at      TIMESTAMP     NOT NULL DEFAULT now(),
    updated_at      TIMESTAMP     NOT NULL DEFAULT now()
);

CREATE UNIQUE INDEX idx_project_name ON projects (name);
CREATE UNIQUE INDEX idx_project_code ON projects (code);
CREATE INDEX idx_project_status ON projects (status);
CREATE INDEX idx_project_start_date ON projects (start_date);
