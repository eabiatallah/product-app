package com.eaa.product_app.mapper;

import com.eaa.product_app.dto.ProductRequest;
import com.eaa.product_app.dto.ProductResponse;
import com.eaa.product_app.model.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Converts between {@link Product} entities and their API-facing DTOs.
 */
public final class ProductMapper {

    private ProductMapper() {
    }

    public static Product toEntity(ProductRequest request) {
        Product product = new Product();
        product.setName(request.name());
        product.setDescription(request.description());
        product.setSubCategoryGuid(request.subCategoryGuid());
        if (request.attributes() != null) {
            product.setAttributes(request.attributes());
        }
        if (request.images() != null) {
            product.setImages(request.images());
        }
        if (request.variants() != null) {
            List<com.eaa.product_app.model.Variant> variants = new ArrayList<>();
            request.variants().forEach(variantRequest -> variants.add(VariantMapper.toEntity(variantRequest)));
            product.setVariants(variants);
        }
        return product;
    }

    public static ProductResponse toResponse(Product product) {
        List<com.eaa.product_app.dto.VariantResponse> variants = product.getVariants() == null
                ? Collections.emptyList()
                : product.getVariants().stream().map(VariantMapper::toResponse).toList();
        return new ProductResponse(
                product.getId(),
                product.getProductGuid(),
                product.getName(),
                product.getDescription(),
                product.getSubCategoryGuid(),
                product.getAttributes(),
                product.getImages(),
                variants,
                product.getCreatedAt(),
                product.getUpdatedAt(),
                product.getCreatedBy());
    }
}
