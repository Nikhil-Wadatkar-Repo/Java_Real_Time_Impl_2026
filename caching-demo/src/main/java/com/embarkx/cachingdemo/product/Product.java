package com.embarkx.cachingdemo.product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA entity mapping to the {@code products} table. Represents a single sellable product
 * with its category and price. Implements {@link Serializable} because instances are also
 * used as Redis cache values (see {@code ProductService} and {@code RedisCacheConfig}), and
 * the Redis client/serializer needs the type to be serializable.
 */
@Entity
@Table(name = "products")
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * No-args constructor required by JPA so the persistence provider can instantiate and
     * hydrate entities via reflection. Not intended for direct use by application code.
     */
    protected Product() {
        // JPA
    }

    /**
     * Creates a new, not-yet-persisted product with the given attributes. The {@code id} and
     * {@code createdAt} fields are left unset here — {@code id} is assigned by the database on
     * insert, and {@code createdAt} is populated by {@link #onCreate()} just before persisting.
     *
     * @param name     the product's display name
     * @param category the category/group the product belongs to
     * @param price    the product's price
     */
    public Product(String name, String category, Double price) {
        this.name = name;
        this.category = category;
        this.price = price;
    }

    /**
     * JPA lifecycle callback invoked automatically immediately before the entity is first
     * inserted. Stamps {@code createdAt} with the current time so callers never need to set it
     * manually, and the column stays immutable afterwards ({@code updatable = false}).
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
