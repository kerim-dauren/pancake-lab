package org.pancakelab.repository.impl;

import org.pancakelab.model.Order;
import org.pancakelab.repository.OrderRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class InMemoryOrderRepository implements OrderRepository {
    private List<Order> orders = new ArrayList<>();

    @Override
    public void save(Order order) {
        orders.add(order);
    }

    @Override
    public Optional<Order> findById(UUID orderId) {
        return orders.stream().filter(o -> o.getId().equals(orderId)).findFirst();
    }

    @Override
    public void deleteById(UUID orderId) {
        orders.removeIf(o -> o.getId().equals(orderId));
    }
}
