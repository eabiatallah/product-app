package com.eaa.product_app.dto;

/**
 * Minimal payload returned when a {@link com.eaa.product_app.model.Product} is created,
 * avoiding the cost/noise of echoing back the full resource (including nested variants/images).
 */
public record ProductCreatedResponse(String productGuid, String description) {
}
