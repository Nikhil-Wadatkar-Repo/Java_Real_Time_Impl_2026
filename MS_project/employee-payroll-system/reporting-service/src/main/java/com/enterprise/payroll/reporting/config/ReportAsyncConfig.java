package com.enterprise.payroll.reporting.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Dedicated pool for report generation, sized for a bursty, I/O-bound
 * workload (waiting on payroll-service's Feign response, not doing CPU
 * work) - so a handful of concurrent report requests don't starve other
 * async work elsewhere in the JVM, and a caller pattern of "10 report
 * requests land at once" degrades via the bounded queue rather than
 * spawning unbounded threads.
 */
@Configuration
public class ReportAsyncConfig {

    @Bean(name = "reportTaskExecutor")
    public TaskExecutor reportTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(8);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("report-exec-");
        executor.setRejectedExecutionHandler(new java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
