package com.enterprise.payroll.notification.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * A record of a notification this service has sent (or attempted to send).
 * {@code sourceEventKey} carries the Kafka key/id of the event that
 * triggered this, giving idempotency: the same event redelivered by Kafka
 * (at-least-once semantics) is detected via
 * {@code idx_notification_dedup} and skipped rather than double-sent.
 */
@Entity
@Table(name = "notifications", indexes = {
        @Index(name = "idx_notification_employee_id", columnList = "employee_id"),
        @Index(name = "idx_notification_dedup", columnList = "type, source_event_key", unique = true)
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 30)
    private NotificationType type;

    @Column(name = "message", nullable = false, length = 1000)
    private String message;

    /** Dedup key - e.g. "employeeId:payMonth" for payroll events. */
    @Column(name = "source_event_key", nullable = false, length = 100)
    private String sourceEventKey;

    @CreationTimestamp
    @Column(name = "sent_at", updatable = false)
    private LocalDateTime sentAt;
}
