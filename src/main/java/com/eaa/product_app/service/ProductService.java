package com.eaa.product_app.service;

import com.eaa.product_app.exception.ResourceNotFoundException;
import com.eaa.product_app.model.Product;
import com.eaa.product_app.model.Variant;
import com.eaa.product_app.repository.ProductRepository;
import com.eaa.product_app.validation.ProductSchemaValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductSchemaValidator productSchemaValidator;

    public ProductService(ProductRepository productRepository, ProductSchemaValidator productSchemaValidator) {
        this.productRepository = productRepository;
        this.productSchemaValidator = productSchemaValidator;
    }

    public Product create(Product product) {
        product.setId(null);
        product.setProductGuid(UUID.randomUUID().toString());
        assignVariantIds(product);
        productSchemaValidator.validate(product);
        return productRepository.save(product);
    }

    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Product findById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No product found with id '" + id + "'"));
    }

    public Product update(String id, Product update) {
        Product existing = findById(id);
        existing.setName(update.getName());
        existing.setDescription(update.getDescription());
        existing.setSubCategoryGuid(update.getSubCategoryGuid());
        existing.setAttributes(update.getAttributes());
        existing.setImages(update.getImages());
        existing.setVariants(update.getVariants());
        assignVariantIds(existing);
        productSchemaValidator.validate(existing);
        return productRepository.save(existing);
    }

    public void delete(String id) {
        findById(id);
        productRepository.deleteById(id);
    }

    public Product addVariant(String productId, Variant variant) {
        Product product = findById(productId);
        variant.setId(UUID.randomUUID().toString());
        product.getVariants().add(variant);
        productSchemaValidator.validate(product);
        return productRepository.save(product);
    }

    public Product updateVariant(String productId, String variantId, Variant update) {
        Product product = findById(productId);
        Variant existing = findVariant(product, variantId);
        existing.setSku(update.getSku());
        existing.setPrice(update.getPrice());
        existing.setStock(update.getStock());
        existing.setAttributes(update.getAttributes());
        existing.setImages(update.getImages());
        productSchemaValidator.validate(product);
        return productRepository.save(product);
    }

    public Product deleteVariant(String productId, String variantId) {
        Product product = findById(productId);
        Variant existing = findVariant(product, variantId);
        product.getVariants().remove(existing);
        return productRepository.save(product);
    }

    private Variant findVariant(Product product, String variantId) {
        return product.getVariants().stream()
                .filter(v -> variantId.equals(v.getId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No variant found with id '" + variantId + "' on product '" + product.getId() + "'"));
    }

    private void assignVariantIds(Product product) {
        if (product.getVariants() == null) {
            return;
        }
        product.getVariants().forEach(variant -> {
            if (variant.getId() == null || variant.getId().isBlank()) {
                variant.setId(UUID.randomUUID().toString());
            }
        });
    }
}
