package com.nt.services;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nt.InventoryClient;
import com.nt.dto.CreateOrderRequest;
import com.nt.dto.InventoryResponse;
import com.nt.dto.OrderResponse;
import com.nt.entity.Order;
import com.nt.repo.OrderRepository;

@Service
public class OrderService {

	private final OrderRepository orderRepository;
	private final InventoryClient inventoryClient;

	public OrderService(OrderRepository orderRepository, InventoryClient inventoryClient) {

		this.orderRepository = orderRepository;
		this.inventoryClient = inventoryClient;
	}

	@Transactional
	public OrderResponse createOrder(CreateOrderRequest request) {

		// 1. Check inventory
		InventoryResponse inventory = inventoryClient.getInventory(request.getProductId());

		// 2. Validate stock
		if (inventory.getAvailableQuantity() < request.getQuantity()) {

			throw new IllegalStateException("Insufficient inventory");
		}

		// 3. Reserve inventory
		// We will call Inventory Service
		// to actually reduce the quantity.

		inventoryClient.reserveInventory(request.getProductId(), request.getQuantity());

		// 4. Create order
		Order order = new Order();

		order.setProductId(request.getProductId());
		order.setQuantity(request.getQuantity());
		order.setStatus("CONFIRMED");
		order.setCreatedAt(LocalDateTime.now());

		// 5. Save order
		Order savedOrder = orderRepository.save(order);

		return new OrderResponse(savedOrder.getId(), savedOrder.getProductId(), savedOrder.getQuantity(),
				savedOrder.getStatus(), savedOrder.getCreatedAt());
	}
}