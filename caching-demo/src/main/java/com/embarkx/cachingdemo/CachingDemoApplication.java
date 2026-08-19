package com.embarkx.cachingdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Entry point for the caching demo application. Bootstraps the Spring Boot application
 * context and turns on Spring's declarative caching support ({@code @EnableCaching}) so
 * that {@code @Cacheable}, {@code @CachePut}, and {@code @CacheEvict} annotations elsewhere
 * in the app (e.g. {@code ProductService}) are actually processed by the container.
 */
@SpringBootApplication
@EnableCaching
public class CachingDemoApplication {

    /**
     * Starts the Spring Boot application: builds the {@link org.springframework.context.ApplicationContext},
     * performs component scanning/auto-configuration, and starts the embedded web server.
     *
     * @param args command-line arguments forwarded to Spring Boot (e.g. {@code --server.port=...})
     */
    public static void main(String[] args) {
        SpringApplication.run(CachingDemoApplication.class, args);
    }
}
