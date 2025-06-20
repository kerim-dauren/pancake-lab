package org.pancakelab.exception;

import java.util.UUID;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(UUID orderId) {
        super(getMessage(orderId));
    }

    private static String getMessage(UUID orderId) {
        return "Order not found:" + orderId;
    }
}
