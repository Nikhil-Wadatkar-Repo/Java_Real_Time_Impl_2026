package com.nt.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nt.dto.InventoryResponse;
import com.nt.entity.Inventory;
import com.nt.exc.InventoryNotFoundException;
import com.nt.repo.InventoryRepository;

@Service
public class InventoryService {

	private final InventoryRepository inventoryRepository;

	public InventoryService(InventoryRepository inventoryRepository) {
		this.inventoryRepository = inventoryRepository;
	}

	public InventoryResponse getInventory(Long productId) {

		Inventory inventory = inventoryRepository.findByProductId(productId)
				.orElseThrow(() -> new InventoryNotFoundException("Inventory not found for product: " + productId));

		return mapToResponse(inventory);
	}

	@Transactional
	public InventoryResponse reserveInventory(Long productId, Integer quantity) {

		Inventory inventory = inventoryRepository.findByProductId(productId)
				.orElseThrow(() -> new InventoryNotFoundException("Inventory not found for product: " + productId));

		if (inventory.getAvailableQuantity() < quantity) {
			throw new IllegalStateException("Insufficient inventory for product: " + productId);
		}

		inventory.setAvailableQuantity(inventory.getAvailableQuantity() - quantity);

		Inventory saved = inventoryRepository.save(inventory);

		return mapToResponse(saved);
	}

	@Transactional
	public InventoryResponse addInventory(Long productId, Integer quantity) {

		Inventory inventory = inventoryRepository.findByProductId(productId)
				.orElseThrow(() -> new InventoryNotFoundException("Inventory not found for product: " + productId));

		inventory.setAvailableQuantity(inventory.getAvailableQuantity() + quantity);

		return mapToResponse(inventoryRepository.save(inventory));
	}

	private InventoryResponse mapToResponse(Inventory inventory) {

		return new InventoryResponse(inventory.getProductId(), inventory.getProductName(),
				inventory.getAvailableQuantity());
	}
}