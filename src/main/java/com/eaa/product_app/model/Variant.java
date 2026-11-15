package com.eaa.product_app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A purchasable variation of a {@link Product} (e.g. a specific size/color combination).
 * The {@code attributes} map holds VARIANT-level attribute values defined by the product's category.
 * Request-shape validation (required fields, formats) is performed on
 * {@link com.eaa.product_app.dto.VariantRequest}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Variant {

    @Id
    private String id;

    private String sku;

    private Double price;

    private Integer stock;

    private Map<String, Object> attributes = new HashMap<>();

    private List<ProductImage> images = new ArrayList<>();
}
