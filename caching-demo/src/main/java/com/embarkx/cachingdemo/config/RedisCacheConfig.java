package com.embarkx.cachingdemo.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Configures the "products" cache to be backed by Redis instead of Spring Boot's default
 * (an unbounded, non-expiring, per-JVM ConcurrentHashMap). A typed TTL ({@link ProductCacheProperties})
 * keeps entries from living in Redis forever, and JSON serialization (rather than Java's default
 * serialization) keeps cached values human-readable in Redis and stable across app restarts/redeploys.
 */
@Configuration
@EnableConfigurationProperties(ProductCacheProperties.class)
public class RedisCacheConfig {

    /**
     * Builds the {@link RedisCacheManager} bean that backs all Spring caches (e.g. the
     * "products" cache used by {@code ProductService}) with Redis, using JSON serialization
     * and a configurable TTL instead of Spring Boot's default in-memory, non-expiring cache.
     *
     * @param connectionFactory the Redis connection factory auto-configured by Spring Boot,
     *                          used to talk to the Redis server
     * @param properties        typed cache settings (currently just TTL) bound from
     *                          {@code product.cache.*} properties
     * @return a {@link RedisCacheManager} configured with JSON value serialization, string key
     *         serialization, null-value caching disabled, and the configured entry TTL
     */
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory, ProductCacheProperties properties) {
        ObjectMapper objectMapper = new ObjectMapper();
        // Needed so LocalDateTime (e.g. Product.createdAt) can be serialized/deserialized to/from JSON.
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        // Embeds the concrete Java type of each cached value into the JSON payload so it can be
        // deserialized back to the correct class later (needed because the cache API only knows
        // about Object at deserialization time). Restricted to subtypes of Object via the
        // polymorphic type validator to avoid unsafe/unbounded deserialization.
        objectMapper.activateDefaultTyping(
                BasicPolymorphicTypeValidator.builder().allowIfSubType(Object.class).build(),
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY);
        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                // Entries expire after the configured TTL so stale data doesn't live in Redis forever.
                .entryTtl(properties.getTtl())
                // A null result (e.g. product not found) is never cached, so lookups for missing
                // ids always re-check the DB rather than caching a permanent "miss".
                .disableCachingNullValues()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jsonSerializer));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(cacheConfiguration)
                .build();
    }
}
