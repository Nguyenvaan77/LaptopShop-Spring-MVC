package com.basis.anhangda37.util;

/**
 * Application-wide constants for consistent naming and configuration values.
 * Centralizes magic strings and numbers used throughout the application.
 */
public class AppConstants {

    // ==================== Role Constants ====================
    public static final String ROLE_USER = "USER";
    public static final String ROLE_ADMIN = "ADMIN";

    // ==================== Order Status Constants ====================
    public static final String ORDER_STATUS_PENDING = "PENDING";
    public static final String ORDER_STATUS_CONFIRMED = "CONFIRMED";
    public static final String ORDER_STATUS_SHIPPED = "SHIPPED";
    public static final String ORDER_STATUS_DELIVERED = "DELIVERED";
    public static final String ORDER_STATUS_CANCELLED = "CANCELLED";

    // ==================== Payment Status Constants ====================
    public static final String PAYMENT_STATUS_PENDING = "PENDING";
    public static final String PAYMENT_STATUS_COMPLETED = "COMPLETED";
    public static final String PAYMENT_STATUS_FAILED = "FAILED";

    // ==================== File Upload Constants ====================
    public static final String UPLOAD_DIR_PRODUCT = "product";
    public static final String UPLOAD_DIR_AVATAR = "avatar";

    // ==================== Session Constants ====================
    public static final String SESSION_CART_SUM = "sum";
    public static final String SESSION_USER_EMAIL = "email";

    // ==================== Pagination Constants ====================
    public static final int DEFAULT_PAGE = 0;

    // ==================== File Constants ====================
    public static final long MAX_FILE_SIZE = 50 * 1024 * 1024; // 50MB

    // ==================== Error Messages ====================
    public static final String ERROR_PRODUCT_NOT_FOUND = "Product not found with id: ";
    public static final String ERROR_USER_NOT_FOUND = "User not found with id: ";
    public static final String ERROR_ORDER_NOT_FOUND = "Order not found with id: ";
    public static final String ERROR_CART_NOT_FOUND = "Cart not found for user: ";
    public static final String ERROR_INVALID_REQUEST = "Invalid request data";

    private AppConstants() {
        // Private constructor to prevent instantiation of constants class
    }
}
