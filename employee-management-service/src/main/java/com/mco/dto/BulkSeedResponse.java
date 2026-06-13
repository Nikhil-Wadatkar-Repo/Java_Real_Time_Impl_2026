package com.mco.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BulkSeedResponse {

    private final int requestedCount;
    private final int insertedCount;
    private final long durationMs;
    private final String message;
}
