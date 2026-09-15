package com.enterprise.payroll.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Single entry point for all client traffic. Routes to business services by
 * Eureka-discovered logical name. Contains cross-cutting concerns only
 * (routing, correlation id propagation, logging, rate limiting) -
 * no business logic belongs here.
 */
@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}
