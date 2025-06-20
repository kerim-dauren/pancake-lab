package org.pancakelab.repository;

import org.pancakelab.model.Order;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing orders.
 * Provides methods to save, find, and delete orders by their ID.
 */
public interface OrderRepository {
    /**
     * Saves an order to the repository.
     *
     * @param order the order to save
     */
    void save(Order order);

    /**
     * Finds an order by its ID.
     *
     * @param orderId the ID of the order to find
     * @return an Optional containing the found order, or empty if not found
     */
    Optional<Order> findById(UUID orderId);

    /**
     * Deletes an order by its ID.
     *
     * @param orderId the ID of the order to delete
     */
    void deleteById(UUID orderId);
}
