-- Indexing rationale:
--   idx_notification_employee_id  "notifications for employee X" lookup
--   idx_notification_dedup        enforces idempotent event processing -
--                                   the same (type, source_event_key) can only
--                                   be inserted once, so a redelivered Kafka
--                                   event is a harmless no-op, not a duplicate email
CREATE TABLE notifications (
    id                BIGSERIAL PRIMARY KEY,
    employee_id       BIGINT       NOT NULL,
    type              VARCHAR(30)  NOT NULL,
    message           VARCHAR(1000) NOT NULL,
    source_event_key  VARCHAR(100) NOT NULL,
    sent_at           TIMESTAMP    NOT NULL DEFAULT now()
);

CREATE INDEX idx_notification_employee_id ON notifications (employee_id);
CREATE UNIQUE INDEX idx_notification_dedup ON notifications (type, source_event_key);
