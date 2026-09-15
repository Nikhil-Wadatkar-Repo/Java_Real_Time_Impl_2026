package com.nt.dto;

public class InventoryResponse {

    private Long productId;
    private String productName;
    private Integer availableQuantity;

    public InventoryResponse() {
    }

    public InventoryResponse(Long productId,
                             String productName,
                             Integer availableQuantity) {
        this.productId = productId;
        this.productName = productName;
        this.availableQuantity = availableQuantity;
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