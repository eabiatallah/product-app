package com.eaa.product_app.exception;

/**
 * Thrown when a requested entity (category, product, variant) does not exist.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
