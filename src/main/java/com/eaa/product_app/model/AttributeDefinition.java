package com.eaa.product_app.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Defines a single attribute within a {@link Category} schema: its name, data type,
 * the level it applies to (product or variant) and the constraints used to validate values.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttributeDefinition {

    @NotBlank
    @Size(max = 50)
    @Pattern(regexp = "^[a-zA-Z0-9 @#_-]+$", message = "name must be alphanumeric (whitespace, -, @, #, _ allowed)")
    private String name;

    @NotNull
    private AttributeType type;

    @NotNull
    private AttributeLevel level;

    private boolean required;

    // Marks a VARIANT-level attribute as a differentiator (e.g. color, size) that
    // contributes to the variant combination uniqueness check and counts against
    // the owning category's variantPolicy.maxAxes.
    private boolean variantAxis;

    // Only relevant when type == ENUM
    private List<String> allowedValues;

    // Only relevant when type == NUMBER
    private Double min;
    private Double max;

    // Only relevant when type == STRING
    private String regex;
}
