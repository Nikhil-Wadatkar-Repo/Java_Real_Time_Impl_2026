package com.nt.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;

    private String productName;

    private Integer availableQuantity;

    public Inventory() {
    }

    public Inventory(Long id,
                     Long productId,
                     String productName,
                     Integer availableQuantity) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.availableQuantity = availableQuantity;
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }
}