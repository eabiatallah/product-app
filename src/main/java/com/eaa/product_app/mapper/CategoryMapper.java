package com.eaa.product_app.mapper;

import com.eaa.product_app.dto.CategoryRequest;
import com.eaa.product_app.dto.CategoryResponse;
import com.eaa.product_app.model.Category;

/**
 * Converts between {@link Category} entities and their API-facing DTOs.
 */
public final class CategoryMapper {

    private CategoryMapper() {
    }

    public static Category toEntity(CategoryRequest request) {
        Category category = new Category();
        category.setCode(request.code());
        category.setName(request.name());
        category.setDescription(request.description());
        category.setCommonAttributes(request.commonAttributes());
        category.setVariantPolicy(request.variantPolicy());
        return category;
    }

    public static CategoryResponse toResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getCategoryGuid(),
                category.getCode(),
                category.getName(),
                category.getDescription(),
                category.getCommonAttributes(),
                category.getVariantPolicy(),
                category.getCreatedAt(),
                category.getUpdatedAt(),
                category.getCreatedBy());
    }
}
