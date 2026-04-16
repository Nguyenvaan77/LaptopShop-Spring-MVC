package com.basis.anhangda37.exception;

/**
 * Exception thrown when a user entity is not found in the database.
 * This is a domain-specific exception for better error handling and recovery strategies.
 */
public class UserNotFoundException extends RuntimeException {
    
    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotFoundException(Long userId) {
        super("User not found with id: " + userId);
    }

    public UserNotFoundException(String fieldName, String fieldValue) {
        super("User not found with " + fieldName + ": " + fieldValue);
    }
}
