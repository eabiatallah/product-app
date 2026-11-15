package com.eaa.product_app.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
import java.util.List;

/**
 * Top-level navigational grouping (e.g. "Clothes", "Electronics", "Furniture").
 * {@code commonAttributes} are shared by every {@link SubCategory} under this category
 * (e.g. "brand" for all Electronics); a sub-category's own {@code attributeDefinitions}
 * take precedence when a name collides. {@code variantPolicy} governs variant-axis
 * constraints (max axis count, duplicate combination checks) for all its sub-categories.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("categories")
public class Category {

    @Id
    private String id;

    @Indexed(unique = true)
    private String categoryGuid;

    @NotBlank
    @Indexed(unique = true)
    private String code;

    @NotBlank
    @Indexed(unique = true)
    private String name;

    private String description;

    @Valid
    private List<AttributeDefinition> commonAttributes;

    private VariantPolicy variantPolicy;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    @CreatedBy
    private String createdBy;
}
