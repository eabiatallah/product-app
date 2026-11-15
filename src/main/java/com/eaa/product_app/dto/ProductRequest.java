package com.eaa.product_app.dto;

import com.eaa.product_app.model.ProductImage;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Map;

/**
 * Client-supplied payload for creating/updating a {@link com.eaa.product_app.model.Product}.
 * Deliberately excludes {@code id}: it is server-assigned and must never be settable by clients.
 * Variants are nested as {@link VariantRequest} so client-supplied variant ids are also rejected.
 */
public record ProductRequest(
        @NotBlank
        @Size(max = 50)
        @Pattern(regexp = "^[a-zA-Z0-9 @#_-]+$", message = "name must be alphanumeric (whitespace, -, @, #, _ allowed)")
        String name,

        @NotBlank
        @Size(max = 50)
        @Pattern(regexp = "^[a-zA-Z0-9 @#_-]+$", message = "description must be alphanumeric (whitespace, -, @, #, _ allowed)")
        String description,

        @NotBlank String subCategoryGuid,
        Map<String, Object> attributes,
        @Valid List<ProductImage> images,
        @Valid List<VariantRequest> variants) {
}
