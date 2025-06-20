package org.pancakelab.service.impl;

import org.pancakelab.model.OrderState;
import org.pancakelab.service.OrderStateService;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In-memory implementation of the OrderStateService interface.
 * This class provides a thread-safe way to manage order states in memory.
 */
public class InMemoryOrderStateService implements OrderStateService {
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
