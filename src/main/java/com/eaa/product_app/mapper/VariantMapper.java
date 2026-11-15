package com.eaa.product_app.mapper;

import com.eaa.product_app.dto.VariantRequest;
import com.eaa.product_app.dto.VariantResponse;
import com.eaa.product_app.model.Variant;

/**
 * Converts between {@link Variant} entities and their API-facing DTOs.
 */
public final class VariantMapper {

    private VariantMapper() {
    }

    public static Variant toEntity(VariantRequest request) {
        Variant variant = new Variant();
        variant.setSku(request.sku());
        variant.setPrice(request.price());
        variant.setStock(request.stock());
        if (request.attributes() != null) {
            variant.setAttributes(request.attributes());
        }
        if (request.images() != null) {
            variant.setImages(request.images());
        }
        return variant;
    }

    public static VariantResponse toResponse(Variant variant) {
        return new VariantResponse(
                variant.getId(),
                variant.getSku(),
                variant.getPrice(),
                variant.getStock(),
                variant.getAttributes(),
                variant.getImages());
    }
}
