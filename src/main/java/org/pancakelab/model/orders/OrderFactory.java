package org.pancakelab.model.orders;

/**
 * Factory interface for creating Order instances.
 * This interface defines a method to create an order with specified building and room numbers.
 */
public interface OrderFactory {
    /**
     * Creates an Order instance with the specified building and room numbers.
     *
     * @param building the building number
     * @param room     the room number
     * @return a new Order instance
     */
    Order createOrder(int building, int room);
}
