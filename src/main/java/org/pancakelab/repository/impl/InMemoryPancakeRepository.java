package org.pancakelab.repository.impl;

import org.pancakelab.model.pancakes.PancakeRecipe;
import org.pancakelab.repository.PancakeRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryPancakeRepository implements PancakeRepository {

    private final Map<UUID, List<PancakeRecipe>> orderPancakes = new ConcurrentHashMap<>();

    @Override
    public List<String> viewOrderPancakes(UUID orderId) {
        return orderPancakes.getOrDefault(orderId, Collections.emptyList())
                .stream()
                .map(PancakeRecipe::description)
                .toList();
    }

    @Override
    public int addPancake(UUID orderId, PancakeRecipe pancake) {
        if (!orderPancakes.containsKey(orderId)) {
            orderPancakes.put(orderId, new ArrayList<>());
        }
        orderPancakes.get(orderId).add(pancake);
        return orderPancakes.get(orderId).size();
    }

    @Override
    public void remove(UUID orderId) {
        orderPancakes.remove(orderId);
    }

    @Override
    public int removePancakes(UUID orderId, String description, int count) {
        List<PancakeRecipe> list = orderPancakes.get(orderId);
        if (list == null || list.isEmpty()) {
            return 0;
        }

        AtomicInteger removedCount = new AtomicInteger(0);
        Iterator<PancakeRecipe> iter = list.iterator();

        while (iter.hasNext() && removedCount.get() < count) {
            PancakeRecipe p = iter.next();
            if (p.description().equals(description)) {
                iter.remove();
                removedCount.incrementAndGet();
            }
        }

        if (list.isEmpty()) {
            orderPancakes.remove(orderId);
        }

        return removedCount.get();
    }

    @Override
    public int getPancakesCount(UUID orderId) {
        return orderPancakes.getOrDefault(orderId, Collections.emptyList()).size();
    }
}
