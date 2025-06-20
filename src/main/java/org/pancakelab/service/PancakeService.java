package org.pancakelab.service;

import org.pancakelab.model.Order;
import org.pancakelab.model.pancakes.*;
import org.pancakelab.repository.OrderRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class PancakeService {
    private Set<UUID> completedOrders = new HashSet<>();
    private Set<UUID> preparedOrders = new HashSet<>();
    private List<PancakeRecipe> pancakes = new ArrayList<>();
    private final OrderRepository orderRepository;

    public PancakeService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order createOrder(int building, int room) {
        Order order = new Order(building, room);
        orderRepository.save(order);
        return order;
    }

    public void addDarkChocolatePancake(UUID orderId, int count) {
        for (int i = 0; i < count; ++i) {
            addPancake(new DarkChocolatePancake(), orderRepository.findById(orderId).get());
        }
    }

    public void addDarkChocolateWhippedCreamPancake(UUID orderId, int count) {
        for (int i = 0; i < count; ++i) {
            addPancake(new DarkChocolateWhippedCreamPancake(), orderRepository.findById(orderId).get());
        }
    }

    public void addDarkChocolateWhippedCreamHazelnutsPancake(UUID orderId, int count) {
        for (int i = 0; i < count; ++i) {
            addPancake(new DarkChocolateWhippedCreamHazelnutsPancake(), orderRepository.findById(orderId).get());
        }
    }

    public void addMilkChocolatePancake(UUID orderId, int count) {
        for (int i = 0; i < count; ++i) {
            addPancake(new MilkChocolatePancake(), orderRepository.findById(orderId).get());
        }
    }

    public void addMilkChocolateHazelnutsPancake(UUID orderId, int count) {
        for (int i = 0; i < count; ++i) {
            addPancake(new MilkChocolateHazelnutsPancake(), orderRepository.findById(orderId).get());
        }
    }

    public List<String> viewOrder(UUID orderId) {
        return pancakes.stream()
                .filter(pancake -> pancake.getOrderId().equals(orderId))
                .map(PancakeRecipe::description).toList();
    }

    private void addPancake(PancakeRecipe pancake, Order order) {
        pancake.setOrderId(order.getId());
        pancakes.add(pancake);

        OrderLog.logAddPancake(order, pancake.description(), pancakes);
    }

    public void removePancakes(String description, UUID orderId, int count) {
        final AtomicInteger removedCount = new AtomicInteger(0);
        pancakes.removeIf(pancake -> {
            return pancake.getOrderId().equals(orderId) &&
                    pancake.description().equals(description) &&
                    removedCount.getAndIncrement() < count;
        });

        Order order = orderRepository.findById(orderId).get();
        OrderLog.logRemovePancakes(order, description, removedCount.get(), pancakes);
    }

    public void cancelOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId).get();
        OrderLog.logCancelOrder(order, this.pancakes);

        pancakes.removeIf(pancake -> pancake.getOrderId().equals(orderId));
        orderRepository.deleteById(orderId);
        completedOrders.removeIf(u -> u.equals(orderId));
        preparedOrders.removeIf(u -> u.equals(orderId));

        OrderLog.logCancelOrder(order, pancakes);
    }

    public void completeOrder(UUID orderId) {
        completedOrders.add(orderId);
    }

    public Set<UUID> listCompletedOrders() {
        return completedOrders;
    }

    public void prepareOrder(UUID orderId) {
        preparedOrders.add(orderId);
        completedOrders.removeIf(u -> u.equals(orderId));
    }

    public Set<UUID> listPreparedOrders() {
        return preparedOrders;
    }

    public Object[] deliverOrder(UUID orderId) {
        if (!preparedOrders.contains(orderId)) return null;

        Order order = orderRepository.findById(orderId).get();
        List<String> pancakesToDeliver = viewOrder(orderId);
        OrderLog.logDeliverOrder(order, this.pancakes);

        pancakes.removeIf(pancake -> pancake.getOrderId().equals(orderId));
        orderRepository.deleteById(orderId);
        preparedOrders.removeIf(u -> u.equals(orderId));

        return new Object[]{order, pancakesToDeliver};
    }
}
