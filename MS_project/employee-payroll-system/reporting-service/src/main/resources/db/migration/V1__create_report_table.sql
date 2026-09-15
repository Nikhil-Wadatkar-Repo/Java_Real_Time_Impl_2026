-- idx_report_status: dashboards/polling for "reports still PENDING/IN_PROGRESS"
CREATE TABLE reports (
    id              BIGSERIAL PRIMARY KEY,
    type            VARCHAR(30)   NOT NULL,
    parameter       VARCHAR(50)   NOT NULL,
    status          VARCHAR(20)   NOT NULL,
    result_summary  VARCHAR(2000),
    error_message   VARCHAR(500),
    created_at      TIMESTAMP     NOT NULL DEFAULT now(),
    completed_at    TIMESTAMP
);

CREATE INDEX idx_report_status ON reports (status);
