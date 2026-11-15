package com.eaa.product_app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Governs how variant "axis" attributes (e.g. color, size) behave for all
 * products/sub-categories under a {@link Category}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VariantPolicy {

    // Maximum number of distinct VARIANT-level attributes flagged as variantAxis=true
    // that a sub-category under this category may define. Null means unlimited.
    private Integer maxAxes;

    // When true, a product may not have two variants sharing the same combination
    // of variantAxis attribute values (e.g. two variants both color=RED, size=M).
    private boolean uniqueCombination;
}
