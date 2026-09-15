package com.enterprise.payroll.payroll.event;

import com.enterprise.payroll.payroll.config.KafkaTopicConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Fire-and-forget publisher: payroll processing has already been committed
 * to the database by the time this is called, so a Kafka outage must never
 * fail the HTTP response - it only means notification-service finds out
 * late (or via a future reconciliation job), not that payroll failed.
 */
@Component
public class PayrollEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(PayrollEventPublisher.class);

    private final KafkaTemplate<String, PayrollCompletedEvent> kafkaTemplate;

    public PayrollEventPublisher(KafkaTemplate<String, PayrollCompletedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishPayrollCompleted(PayrollCompletedEvent event) {
        kafkaTemplate.send(KafkaTopicConfig.PAYROLL_COMPLETED_TOPIC, String.valueOf(event.employeeId()), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish payroll.completed for employeeId={}", event.employeeId(), ex);
                    } else {
                        log.info("Published payroll.completed for employeeId={} partition={} offset={}",
                                event.employeeId(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }
                });
    }
}
