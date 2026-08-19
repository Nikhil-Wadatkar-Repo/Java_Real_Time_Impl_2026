package com.embarkx.cachingdemo.product;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Service layer for {@link Product} CRUD operations, and the layer where Redis caching is
 * declared via Spring's {@code @Cacheable}/{@code @CachePut}/{@code @CacheEvict} annotations.
 * <p>
 * Reads are cached in Redis under the "products" cache. Every DB hit logs a line so cache
 * hits vs. misses are visible in the console: call GET /api/products/{id} twice and the
 * "Fetching product ... from DB" log should only appear once.
 */
@Service
public class ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);
    // Cache name used consistently across all cache annotations in this class so the
    // key namespaces for individual products ("#id") and the "all products" list ("'all'")
    // live together under one Redis cache region.
    private static final String PRODUCT_CACHE = "products";

    private final ProductRepository productRepository;

    /**
     * Creates the service, wiring in the repository used for all database access.
     *
     * @param productRepository repository used to persist and query {@link Product} entities
     */
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Persists a new product to the database. After insertion, the resulting entity (with its
     * generated id) is written into the cache under its own id key ({@code @CachePut}), so a
     * subsequent {@link #getProductById(UUID)} call is a cache hit without an extra DB read.
     * The cached "all products" list is evicted ({@code @CacheEvict}) because it is now stale.
     *
     * @param name     the product's display name
     * @param category the category/group the product belongs to
     * @param price    the product's price
     * @return the newly created and persisted {@link Product}, including its generated id
     */
    @CachePut(value = PRODUCT_CACHE, key = "#result.id")
    @CacheEvict(value = PRODUCT_CACHE, key = "'all'")
    public Product createProduct(String name, String category, Double price) {
        log.info("Inserting product '{}' into DB", name);
        return productRepository.save(new Product(name, category, price));
    }

    /**
     * Retrieves every product. The result is cached as a whole under the fixed key {@code 'all'}
     * so repeated calls avoid hitting the database until the cache is evicted (on create/update/delete).
     *
     * @return the list of all products currently in the database, or an empty list if none exist
     */
    @Cacheable(value = PRODUCT_CACHE, key = "'all'")
    public List<Product> getAllProducts() {
        log.info("Fetching all products from DB");
        return productRepository.findAll();
    }

    /**
     * Retrieves a single product by id, checking the cache first (keyed by the id itself) before
     * falling back to the database. {@code disableCachingNullValues()} in {@code RedisCacheConfig}
     * means a "not found" result is not cached, so a missing id will always re-query the DB.
     *
     * @param id the product's unique identifier
     * @return the matching {@link Product}, or {@code null} if no product with that id exists
     */
    @Cacheable(value = PRODUCT_CACHE, key = "#id")
    public Product getProductById(UUID id) {
        log.info("Fetching product {} from DB", id);
        return productRepository.findById(id).orElse(null);
    }

    /**
     * Updates an existing product's mutable fields. If the product does not exist, no changes
     * are made and no cache entry is written for it (Spring only applies {@code @CachePut} using
     * the returned value, and a {@code null} return combined with cache-null-disabled means
     * nothing is cached). On success, the "all products" cache is evicted since it is now stale.
     *
     * @param id       the id of the product to update
     * @param name     the new display name
     * @param category the new category/group
     * @param price    the new price
     * @return the updated {@link Product}, or {@code null} if no product with the given id exists
     */
    @CachePut(value = PRODUCT_CACHE, key = "#id")
    @CacheEvict(value = PRODUCT_CACHE, key = "'all'")
    public Product updateProduct(UUID id, String name, String category, Double price) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            // Nothing to update — return early so we don't attempt to save a non-existent entity.
            return null;
        }
        log.info("Updating product {} in DB", id);
        product.setName(name);
        product.setCategory(category);
        product.setPrice(price);
        return productRepository.save(product);
    }

    /**
     * Deletes a product by id and evicts both the per-id cache entry and the "all products"
     * cache entry, so neither reflects the now-deleted product on the next read.
     *
     * @param id the id of the product to delete
     */
    @Caching(evict = {
            @CacheEvict(value = PRODUCT_CACHE, key = "#id"),
            @CacheEvict(value = PRODUCT_CACHE, key = "'all'")
    })
    public void deleteProduct(UUID id) {
        log.info("Deleting product {} from DB", id);
        productRepository.deleteById(id);
    }
}
