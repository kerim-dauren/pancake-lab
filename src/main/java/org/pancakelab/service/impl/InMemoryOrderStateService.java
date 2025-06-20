package org.pancakelab.service.impl;

import org.pancakelab.model.OrderState;
import org.pancakelab.service.OrderStateService;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryOrderStateService implements OrderStateService {
    // Thread-safe map of orderId → OrderState
    private final Map<UUID, OrderState> orderStates = new ConcurrentHashMap<>();

    @Override
    public OrderState get(UUID orderId) {
        return orderStates.get(orderId);
    }

    @Override
    public void update(UUID orderId, OrderState state) {
        orderStates.put(orderId, state);
    }

    @Override
    public void remove(UUID orderId) {
        orderStates.remove(orderId);
    }

    @Override
    public Set<UUID> getOrderIdsByState(OrderState state) {
        return orderStates.entrySet().stream()
                .filter(entry -> entry.getValue() == state)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }
}
