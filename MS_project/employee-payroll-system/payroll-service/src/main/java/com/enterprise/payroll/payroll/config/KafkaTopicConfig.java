package com.enterprise.payroll.payroll.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/** Declares the topic so a fresh broker auto-creates it with sane defaults instead of relying on broker auto-create config. */
@Configuration
public class KafkaTopicConfig {

    public static final String PAYROLL_COMPLETED_TOPIC = "payroll.completed";

    @Bean
    public NewTopic payrollCompletedTopic() {
        return TopicBuilder.name(PAYROLL_COMPLETED_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
