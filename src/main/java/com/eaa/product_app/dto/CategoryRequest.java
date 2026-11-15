package com.eaa.product_app.dto;

import com.eaa.product_app.model.AttributeDefinition;
import com.eaa.product_app.model.VariantPolicy;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * Client-supplied payload for creating/updating a {@link com.eaa.product_app.model.Category}.
 * Deliberately excludes {@code id}: it is server-assigned and must never be settable by clients.
 */
public record CategoryRequest(
        @NotBlank
        @Size(max = 50)
        @Pattern(regexp = "^[a-zA-Z0-9 @#_-]+$", message = "code must be alphanumeric (whitespace, -, @, #, _ allowed)")
        String code,

        @NotBlank
        @Size(max = 50)
        @Pattern(regexp = "^[a-zA-Z0-9 @#_-]+$", message = "name must be alphanumeric (whitespace, -, @, #, _ allowed)")
        String name,

        @NotBlank
        @Size(max = 50)
        @Pattern(regexp = "^[a-zA-Z0-9 @#_-]+$", message = "description must be alphanumeric (whitespace, -, @, #, _ allowed)")
        String description,

        @Valid List<AttributeDefinition> commonAttributes,
        VariantPolicy variantPolicy) {
}
