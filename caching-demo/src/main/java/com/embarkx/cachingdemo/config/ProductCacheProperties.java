package com.embarkx.cachingdemo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * Type-safe configuration binding for cache-related settings, bound from properties prefixed
 * with {@code product.cache} (e.g. {@code product.cache.ttl} in application.yml/.properties).
 * Consumed by {@link RedisCacheConfig} to configure the Redis "products" cache's time-to-live.
 * The getter/setter pair below is plain accessor boilerplate required for Spring Boot's
 * relaxed property binding — no additional logic is involved.
 */
@ConfigurationProperties(prefix = "product.cache")
public class ProductCacheProperties {

    /** How long an entry stays in Redis before expiring. */
    private Duration ttl = Duration.ofMinutes(10);

    public Duration getTtl() {
        return ttl;
    }

    public void setTtl(Duration ttl) {
        this.ttl = ttl;
    }
}
