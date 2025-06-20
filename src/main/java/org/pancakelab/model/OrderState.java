package org.pancakelab.model;

/**
 * Represents the state of an order in the Pancake Lab.
 * The states include:
 * - CREATED: The order has been created but not yet processed.
 * - COMPLETED: The order is completed and ready for preparing.
 * - PREPARED: The order has been fully prepared and is ready for delivery.
 */
public enum OrderState {
    CREATED,
    COMPLETED,
    PREPARED
}
