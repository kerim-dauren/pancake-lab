package org.pancakelab.exception;

import java.util.UUID;

/**
 * Custom exception class for order not found errors.
 * This exception is thrown when an order with a specific ID cannot be found.
 */
public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(UUID orderId) {
        super("Order not found:" + orderId);
    }
}
