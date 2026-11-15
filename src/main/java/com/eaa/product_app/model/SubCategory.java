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
 * Defines the attribute schema (product-level and variant-level) that all products
 * and their variants belonging to this sub-category must conform to
 * (e.g. "T-Shirts", "Jeans" under the "Clothes" category). The effective schema is
 * the owning {@link Category}'s {@code commonAttributes} merged with this sub-category's
 * own {@code attributeDefinitions}, which take precedence on a name collision.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("subcategories")
public class SubCategory {

    @Id
    private String id;

    @Indexed(unique = true)
    private String subCategoryGuid;

    @NotBlank
    private String name;

    private String description;

    @NotBlank
    private String categoryGuid;

    @Valid
    private List<AttributeDefinition> attributeDefinitions;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    @CreatedBy
    private String createdBy;
}
