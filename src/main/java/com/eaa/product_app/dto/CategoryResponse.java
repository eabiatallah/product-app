package com.eaa.product_app.dto;

import com.eaa.product_app.model.AttributeDefinition;
import com.eaa.product_app.model.VariantPolicy;

import java.time.Instant;
import java.util.List;

/**
 * API representation of a persisted {@link com.eaa.product_app.model.Category}.
 */
public record CategoryResponse(
        String id,
        String categoryGuid,
        String code,
        String name,
        String description,
        List<AttributeDefinition> commonAttributes,
        VariantPolicy variantPolicy,
        Instant createdAt,
        Instant updatedAt,
        String createdBy) {
}
