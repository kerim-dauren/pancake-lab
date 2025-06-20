package org.pancakelab.exception;

/**
 * Custom exception class for validation errors.
 * This exception is thrown when input validation fails.
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
