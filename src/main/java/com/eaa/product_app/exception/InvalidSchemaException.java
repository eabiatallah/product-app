package com.eaa.product_app.exception;

/**
 * Thrown when a Category/SubCategory attribute schema is internally invalid
 * (e.g. a sub-category defines more variant-axis attributes than its category's
 * variantPolicy.maxAxes allows).
 */
public class InvalidSchemaException extends RuntimeException {

    public InvalidSchemaException(String message) {
        super(message);
    }
}
