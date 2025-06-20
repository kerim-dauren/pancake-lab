package org.pancakelab.repository.impl;

import org.pancakelab.model.pancakes.PancakeRecipe;
import org.pancakelab.repository.PancakeRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class InMemoryPancakeRepository implements PancakeRepository {
    // Thread-safe map of orderId → Deque of PancakeRecipe
    private final Map<UUID, ConcurrentLinkedDeque<PancakeRecipe>> orderPancakes = new ConcurrentHashMap<>();

    @Override
    public List<String> viewOrderPancakes(UUID orderId) {
        var deque = orderPancakes.get(orderId);
        if (deque == null) return List.of();
        return deque.stream()
                .map(PancakeRecipe::description)
                .toList();
    }

    @Override
    public int addPancake(UUID orderId, PancakeRecipe pancake) {
        var deque = orderPancakes.computeIfAbsent(orderId, id -> new ConcurrentLinkedDeque<>());
        deque.add(pancake);
        return deque.size();
    }

    @Override
    public void remove(UUID orderId) {
        orderPancakes.remove(orderId);
    }

    @Override
    public int removePancakes(UUID orderId, String description, int count) {
        var deque = orderPancakes.get(orderId);
        if (deque == null || deque.isEmpty()) return 0;

        int removed = 0;
        for (var it = deque.iterator(); it.hasNext() && removed < count; ) {
            if (it.next().description().equals(description)) {
                it.remove();
                removed++;
            }
        }
        if (deque.isEmpty()) {
            orderPancakes.remove(orderId, deque);
        }
        return removed;
    }

    @Override
    public int getPancakesCount(UUID orderId) {
        var deque = orderPancakes.get(orderId);
        return deque == null ? 0 : deque.size();
    }
}
