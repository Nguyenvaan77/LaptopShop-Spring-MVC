package com.basis.anhangda37.exception;

/**
 * Exception thrown when an order entity is not found in the database.
 * This is a domain-specific exception for better error handling and recovery strategies.
 */
public class OrderNotFoundException extends RuntimeException {
    
    public OrderNotFoundException(String message) {
        super(message);
    }

    public OrderNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrderNotFoundException(Long orderId) {
        super("Order not found with id: " + orderId);
    }
}
