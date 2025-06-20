package org.pancakelab.service.impl;

import org.pancakelab.model.OrderState;
import org.pancakelab.service.OrderStateManager;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryOrderStateManager implements OrderStateManager {

    private final Map<UUID, OrderState> orderStatuses = new ConcurrentHashMap<>();

    @Override
    public OrderState get(UUID orderId) {
        return orderStatuses.get(orderId);
    }

    @Override
    public void update(UUID orderId, OrderState state) {
        orderStatuses.put(orderId, state);
    }

    @Override
    public void remove(UUID orderId) {
        orderStatuses.remove(orderId);
    }

    @Override
    public Set<UUID> getOrderIdsByState(OrderState state) {
        return orderStatuses.entrySet().stream()
                .filter(entry -> entry.getValue() == state)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }
}
