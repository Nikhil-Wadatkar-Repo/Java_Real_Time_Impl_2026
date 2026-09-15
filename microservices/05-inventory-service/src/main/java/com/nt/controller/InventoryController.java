package com.nt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nt.dto.InventoryResponse;
import com.nt.dto.UpdateStockRequest;
import com.nt.service.InventoryService;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/{productId}")
    public ResponseEntity<InventoryResponse> getInventory(
            @PathVariable Long productId) {

        return ResponseEntity.ok(
                inventoryService.getInventory(productId)
        );
    }

    @PostMapping("/{productId}/reserve")
    public ResponseEntity<InventoryResponse> reserveInventory(
            @PathVariable Long productId,
            @RequestBody UpdateStockRequest request) {

        return ResponseEntity.ok(
                inventoryService.reserveInventory(
                        productId,
                        request.getQuantity()
                )
        );
    }

    @PostMapping("/{productId}/add")
    public ResponseEntity<InventoryResponse> addInventory(
            @PathVariable Long productId,
            @RequestBody UpdateStockRequest request) {

        return ResponseEntity.ok(
                inventoryService.addInventory(
                        productId,
                        request.getQuantity()
                )
        );
    }
}