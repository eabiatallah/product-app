package com.eaa.product_app.mapper;

import com.eaa.product_app.dto.SubCategoryRequest;
import com.eaa.product_app.dto.SubCategoryResponse;
import com.eaa.product_app.model.SubCategory;

/**
 * Converts between {@link SubCategory} entities and their API-facing DTOs.
 */
public final class SubCategoryMapper {

    private SubCategoryMapper() {
    }

    public static SubCategory toEntity(SubCategoryRequest request) {
        SubCategory subCategory = new SubCategory();
        subCategory.setName(request.name());
        subCategory.setDescription(request.description());
        subCategory.setCategoryGuid(request.categoryGuid());
        subCategory.setAttributeDefinitions(request.attributeDefinitions());
        return subCategory;
    }

    public static SubCategoryResponse toResponse(SubCategory subCategory) {
        return new SubCategoryResponse(
                subCategory.getId(),
                subCategory.getSubCategoryGuid(),
                subCategory.getName(),
                subCategory.getDescription(),
                subCategory.getCategoryGuid(),
                subCategory.getAttributeDefinitions(),
                subCategory.getCreatedAt(),
                subCategory.getUpdatedAt(),
                subCategory.getCreatedBy());
    }
}
