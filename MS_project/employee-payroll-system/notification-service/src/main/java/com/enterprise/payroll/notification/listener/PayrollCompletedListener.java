package com.enterprise.payroll.notification.listener;

import com.enterprise.payroll.notification.entity.Notification;
import com.enterprise.payroll.notification.entity.NotificationType;
import com.enterprise.payroll.notification.event.PayrollCompletedEvent;
import com.enterprise.payroll.notification.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Consumes {@code payroll.completed} and simulates sending an email.
 * Kafka's at-least-once delivery means the same event can arrive twice
 * (e.g. after a consumer rebalance before an offset commit) - the
 * {@code idx_notification_dedup} unique index on (type, sourceEventKey)
 * makes a duplicate delivery a no-op instead of a duplicate email.
 */
@Component
public class PayrollCompletedListener {

    private static final Logger log = LoggerFactory.getLogger(PayrollCompletedListener.class);

    private final NotificationRepository notificationRepository;

    public PayrollCompletedListener(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @KafkaListener(topics = "payroll.completed", groupId = "notification-service")
    @Transactional
    public void onPayrollCompleted(PayrollCompletedEvent event) {
        String dedupKey = event.employeeId() + ":" + event.payMonth();

        if (notificationRepository.existsByTypeAndSourceEventKey(NotificationType.PAYROLL_COMPLETED, dedupKey)) {
            log.info("Duplicate payroll.completed event for {} - already notified, skipping", dedupKey);
            return;
        }

        String message = "Your payroll for " + event.payMonth() + " has been processed. Net salary: "
                + event.netSalary();

        notificationRepository.save(Notification.builder()
                .employeeId(event.employeeId())
                .type(NotificationType.PAYROLL_COMPLETED)
                .message(message)
                .sourceEventKey(dedupKey)
                .build());

        // Simulated send - a real integration (SES, SendGrid, ...) would be called here.
        log.info("Sent payroll-completed notification to employeeId={}: {}", event.employeeId(), message);
    }
}
