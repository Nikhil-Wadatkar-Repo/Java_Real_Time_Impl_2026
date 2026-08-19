package com.embarkx.cachingdemo.product;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller exposing CRUD endpoints for {@link Product} under {@code /api/products}.
 * Delegates all business logic and cache management to {@link ProductService}; this class is
 * a thin HTTP adapter that maps request parameters/path variables to service calls.
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    /**
     * Creates the controller, wiring in the service used to fulfill all requests.
     *
     * @param productService service handling product persistence and caching
     */
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Creates a new product from request parameters.
     *
     * @param name     the product's display name
     * @param category the category/group the product belongs to
     * @param price    the product's price
     * @return the newly created {@link Product}, including its generated id
     */
    @PostMapping
    public Product createProduct(@RequestParam String name,
            @RequestParam String category,
            @RequestParam Double price) {
        return productService.createProduct(name, category, price);
    }

    /**
     * Lists every product currently stored.
     *
     * @return the list of all products, or an empty list if none exist
     */
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    /**
     * Retrieves a single product by its id.
     *
     * @param id the product's unique identifier, taken from the URL path
     * @return the matching {@link Product}, or {@code null} (serialized as an empty/absent
     *         response body) if no product with that id exists
     */
    @GetMapping("/{id}")
    public Product getProduct(@PathVariable UUID id) {
        return productService.getProductById(id);
    }

    /**
     * Updates an existing product's fields.
     *
     * @param id       the id of the product to update, taken from the URL path
     * @param name     the new display name
     * @param category the new category/group
     * @param price    the new price
     * @return the updated {@link Product}, or {@code null} if no product with the given id exists
     */
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable UUID id,
            @RequestParam String name,
            @RequestParam String category,
            @RequestParam Double price) {
        return productService.updateProduct(id, name, category, price);
    }

    /**
     * Deletes a product by id.
     *
     * @param id the id of the product to delete, taken from the URL path
     * @return a fixed confirmation message ("Deleted") regardless of whether a matching
     *         product actually existed
     */
    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return "Deleted";
    }
}
