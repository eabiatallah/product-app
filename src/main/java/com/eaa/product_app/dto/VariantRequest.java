package com.eaa.product_app.dto;

import com.eaa.product_app.model.ProductImage;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;
import java.util.Map;

/**
 * Client-supplied payload for creating/updating a {@link com.eaa.product_app.model.Variant}.
 * Deliberately excludes {@code id}: it is server-assigned and must never be settable by clients.
 */
public record VariantRequest(
        @NotBlank String sku,
        @NotNull @PositiveOrZero Double price,
        @NotNull @PositiveOrZero Integer stock,
        Map<String, Object> attributes,
        @Valid List<ProductImage> images) {
}
