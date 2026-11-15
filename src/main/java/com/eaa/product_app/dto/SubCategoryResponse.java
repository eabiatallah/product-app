package com.eaa.product_app.dto;

import com.eaa.product_app.model.AttributeDefinition;

import java.time.Instant;
import java.util.List;

/**
 * API representation of a persisted {@link com.eaa.product_app.model.SubCategory}.
 */
public record SubCategoryResponse(
        String id,
        String subCategoryGuid,
        String name,
        String description,
        String categoryGuid,
        List<AttributeDefinition> attributeDefinitions,
        Instant createdAt,
        Instant updatedAt,
        String createdBy) {
}
