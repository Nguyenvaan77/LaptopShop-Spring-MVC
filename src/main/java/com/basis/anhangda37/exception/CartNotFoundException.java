package com.basis.anhangda37.exception;

/**
 * Exception thrown when a cart entity is not found for a user.
 * This is a domain-specific exception for better error handling and recovery strategies.
 */
public class CartNotFoundException extends RuntimeException {
    
    public CartNotFoundException(String message) {
        super(message);
    }

    public CartNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CartNotFoundException(Long userId) {
        super("Cart not found for user with id: " + userId);
    }
}
