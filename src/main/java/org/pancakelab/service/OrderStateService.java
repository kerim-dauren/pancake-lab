package org.pancakelab.service;

import org.pancakelab.model.OrderState;

import java.util.Set;
import java.util.UUID;

/**
 * Service interface for managing the state of orders.
 * Provides methods to get, update, and remove order states,
 * as well as retrieve order IDs by their state.
 */
public interface OrderStateService {
    /**
     * Retrieves the current state of an order.
     *
     * @param orderId the ID of the order
     * @return the current state of the order
     */
    OrderState get(UUID orderId);

    /**
     * Updates the state of an order.
     *
     * @param orderId the ID of the order
     * @param state   the new state of the order
     */
    void update(UUID orderId, OrderState state);

    /**
     * Removes an order from the cache.
     *
     * @param orderId the ID of the order to remove
     */
    void remove(UUID orderId);

    /**
     * Retrieves all order IDs that are in a specific state.
     *
     * @param state the state to filter orders by
     * @return a set of order IDs that match the specified state
     */
    Set<UUID> getOrderIdsByState(OrderState state);
}
