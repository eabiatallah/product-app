package com.eaa.product_app.dto;

import com.eaa.product_app.model.ProductImage;

import java.util.List;
import java.util.Map;

/**
 * API representation of a persisted {@link com.eaa.product_app.model.Variant}.
 */
public record VariantResponse(
        String id,
        String sku,
        Double price,
        Integer stock,
        Map<String, Object> attributes,
        List<ProductImage> images) {
}
