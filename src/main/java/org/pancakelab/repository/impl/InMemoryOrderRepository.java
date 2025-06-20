package org.pancakelab.repository.impl;

import org.pancakelab.model.Order;
import org.pancakelab.repository.OrderRepository;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * In-memory implementation of the OrderRepository interface.
 * This class provides a thread-safe way to store and retrieve orders in memory.
 */
public class InMemoryOrderRepository implements OrderRepository {
    private final ConcurrentMap<UUID, Order> orders = new ConcurrentHashMap<>();

    @Override
    public void save(Order order) {
        orders.put(order.getId(), order);
    }

    @Override
    public Optional<Order> findById(UUID orderId) {
        return Optional.ofNullable(orders.get(orderId));
    }

    @Override
    public void deleteById(UUID orderId) {
        orders.remove(orderId);
    }
}
