package com.basis.anhangda37.exception;

/**
 * Exception thrown when a product entity is not found in the database.
 * This is a domain-specific exception for better error handling and recovery strategies.
 */
public class ProductNotFoundException extends RuntimeException {
    
    public ProductNotFoundException(String message) {
        super(message);
    }

    public ProductNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProductNotFoundException(Long productId) {
        super("Product not found with id: " + productId);
    }
}
