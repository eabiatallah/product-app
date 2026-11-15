package com.eaa.product_app.dto;

import com.eaa.product_app.model.ProductImage;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * API representation of a persisted {@link com.eaa.product_app.model.Product}.
 */
public record ProductResponse(
        String id,
        String productGuid,
        String name,
        String description,
        String subCategoryGuid,
        Map<String, Object> attributes,
        List<ProductImage> images,
        List<VariantResponse> variants,
        Instant createdAt,
        Instant updatedAt,
        String createdBy) {
}
