package com.basis.anhangda37.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Standard error response format for the application.
 * Provides consistent structure for all error responses across API endpoints.
 * 
 * Fields:
 * - timestamp: The time when the error occurred
 * - status: HTTP status code
 * - error: Error type/category
 * - message: Human-readable error message
 * - validationErrors: Field-level validation errors (if applicable)
 * - path: The request path that caused the error
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private Map<String, String> validationErrors;
    private String path;
}
