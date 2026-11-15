package com.eaa.product_app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A product belonging to a {@link SubCategory}. The {@code attributes} map holds
 * PRODUCT-level attribute values, shared by all of its {@link Variant}s, as
 * defined by the referenced sub-category's schema.
 * Request-shape validation (required fields, formats) is performed on
 * {@link com.eaa.product_app.dto.ProductRequest}; schema/business validation against
 * the referenced category is performed explicitly in
 * {@link com.eaa.product_app.service.ProductService} via
 * {@link com.eaa.product_app.validation.ProductSchemaValidator}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("products")
public class Product {

    @Id
    private String id;

    @Indexed(unique = true)
    private String productGuid;

    private String name;

    private String description;

    private String subCategoryGuid;

    private Map<String, Object> attributes = new HashMap<>();

    private List<ProductImage> images = new ArrayList<>();

    private List<Variant> variants = new ArrayList<>();

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    @CreatedBy
    private String createdBy;
}
