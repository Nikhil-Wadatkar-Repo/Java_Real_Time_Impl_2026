package com.nt;

import java.util.Map;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.nt.dto.InventoryResponse;

@Component
public class InventoryClient {

    private final RestClient restClient;

    public InventoryClient(RestClient restClient) {
        this.restClient = restClient;
    }

    /**
     * Check inventory
     */
    @Retryable(
            retryFor = Exception.class,
            maxAttempts = 3,
            backoff = @Backoff(
                    delay = 1000,
                    multiplier = 2
            )
    )
    public InventoryResponse getInventory(Long productId) {

        System.out.println(
                "Calling Inventory Service - GET inventory"
        );

        return restClient.get()
                .uri("http://localhost:8082/api/inventory/{productId}", productId)
                .retrieve()
                .body(InventoryResponse.class);
    }

    /**
     * Reserve inventory
     */
    @Retryable(
            retryFor = Exception.class,
            maxAttempts = 3,
            backoff = @Backoff(
                    delay = 1000,
                    multiplier = 2
            )
    )
    public InventoryResponse reserveInventory(
            Long productId,
            Integer quantity) {

        System.out.println(
                "Calling Inventory Service - RESERVE inventory"
        );

        return restClient.post()
                .uri("http://localhost:8082/api/inventory/{productId}/reserve", productId)
                .body(Map.of("quantity", quantity))
                .retrieve()
                .body(InventoryResponse.class);
    }

    /**
     * Called after all retry attempts fail.
     */
    @Recover
    public InventoryResponse recover(
            Exception ex,
            Long productId,
            Integer quantity) {

        System.out.println(
                "Inventory Service unavailable after retries"
        );

        throw new RuntimeException(
                "Unable to communicate with Inventory Service",
                ex
        );
    }
}