package com.nt.dto;

import java.time.LocalDateTime;

public class OrderResponse {

    private Long orderId;
    private Long productId;
    private Integer quantity;
    private String status;
    private LocalDateTime createdAt;

    public OrderResponse() {
    }

    public OrderResponse(Long orderId,
                          Long productId,
                          Integer quantity,
                          String status,
                          LocalDateTime createdAt) {

        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}