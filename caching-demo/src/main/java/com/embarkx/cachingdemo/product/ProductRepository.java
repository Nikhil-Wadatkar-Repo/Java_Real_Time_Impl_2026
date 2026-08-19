package com.embarkx.cachingdemo.product;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Spring Data JPA repository for {@link Product} persistence. Inherits standard CRUD
 * operations (save, findById, findAll, delete, etc.) from {@link JpaRepository} without
 * any implementation code — Spring generates the implementation at runtime.
 */
public interface ProductRepository extends JpaRepository<Product, UUID> {

    /**
     * Looks up all products belonging to a given category. The query is derived automatically
     * by Spring Data from the method name (a "category" equality query).
     *
     * @param category the category value to match against {@code Product.category}
     * @return the list of matching products, or an empty list if none match
     */
    List<Product> findByCategory(String category);
}
