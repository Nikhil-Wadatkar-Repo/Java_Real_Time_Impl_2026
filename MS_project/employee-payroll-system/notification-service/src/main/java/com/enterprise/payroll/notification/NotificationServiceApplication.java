package com.enterprise.payroll.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Reacts to platform events (payroll completed, employee created) by
 * recording/sending a notification. Deliberately event-driven (Kafka
 * consumer, not a REST endpoint other services call synchronously) so a
 * slow or unavailable notification path never blocks the service that
 * triggered it.
 */
@SpringBootApplication
public class NotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }
}
