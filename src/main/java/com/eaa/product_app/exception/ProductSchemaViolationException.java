package com.eaa.product_app.exception;

import java.util.Map;

/**
 * Thrown when a {@link com.eaa.product_app.model.Product}'s attributes (or any of its
 * variants' attributes) do not conform to the effective attribute schema of its
 * referenced sub-category/category, as determined by
 * {@link com.eaa.product_app.validation.ProductSchemaValidator}.
 */
public class ProductSchemaViolationException extends RuntimeException {

    private final Map<String, String> errors;

    public ProductSchemaViolationException(Map<String, String> errors) {
        super("Product attributes are invalid for the referenced category");
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
