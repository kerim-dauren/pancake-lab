package org.pancakelab.repository;

import org.pancakelab.model.Order;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    void save(Order order);

    Optional<Order> findById(UUID orderId);

    void deleteById(UUID orderId);
}
