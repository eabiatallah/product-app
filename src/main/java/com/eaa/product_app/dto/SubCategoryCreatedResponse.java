package com.eaa.product_app.dto;

/**
 * Minimal payload returned when a {@link com.eaa.product_app.model.SubCategory} is created,
 * avoiding the cost/noise of echoing back the full resource (including nested schema lists).
 */
public record SubCategoryCreatedResponse(String subCategoryGuid, String name) {
}
