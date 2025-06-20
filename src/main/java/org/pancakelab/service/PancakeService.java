package org.pancakelab.service;

import org.pancakelab.exception.OrderNotFoundException;
import org.pancakelab.model.Order;
import org.pancakelab.model.OrderFactory;
import org.pancakelab.model.OrderState;
import org.pancakelab.model.pancakes.Pancake;
import org.pancakelab.model.pancakes.PancakeRecipe;
import org.pancakelab.repository.OrderRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class PancakeService {
    private List<PancakeRecipe> pancakes = new ArrayList<>();
    private final OrderRepository orderRepository;
    private final OrderFactory orderFactory;
    private final OrderLogger orderLogger;
    private final OrderStateService orderStateService;

    public PancakeService(
            OrderRepository orderRepository,
            OrderFactory orderFactory,
            OrderLogger orderLogger,
            OrderStateService orderStateService
    ) {
        this.orderRepository = orderRepository;
        this.orderFactory = orderFactory;
        this.orderLogger = orderLogger;
        this.orderStateService = orderStateService;
    }

    public Order createOrder(int building, int room) {
        Order order = orderFactory.createOrder(building, room);
        orderRepository.save(order);
        orderStateService.update(order.getId(), OrderState.CREATED);
        return order;
    }

    public void addDarkChocolatePancake(UUID orderId, int count) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
        for (int i = 0; i < count; ++i) {
            addPancake(new Pancake.Builder()
                    .withIngredients(List.of("dark chocolate"))
                    .build(), order);
        }
    }

    public void addDarkChocolateWhippedCreamPancake(UUID orderId, int count) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
        for (int i = 0; i < count; ++i) {
            addPancake(new Pancake.Builder()
                    .withIngredients(List.of("dark chocolate", "whipped cream"))
                    .build(), order);
        }
    }

    public void addDarkChocolateWhippedCreamHazelnutsPancake(UUID orderId, int count) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
        for (int i = 0; i < count; ++i) {
            addPancake(new Pancake.Builder()
                    .withIngredients(List.of("dark chocolate", "whipped cream", "hazelnuts"))
                    .build(), order);
        }
    }

    public void addMilkChocolatePancake(UUID orderId, int count) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
        for (int i = 0; i < count; ++i) {
            addPancake(new Pancake.Builder()
                    .withIngredients(List.of("milk chocolate"))
                    .build(), order);
        }
    }

    public void addMilkChocolateHazelnutsPancake(UUID orderId, int count) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
        for (int i = 0; i < count; ++i) {
            addPancake(new Pancake.Builder()
                    .withIngredients(List.of("milk chocolate", "hazelnuts"))
                    .build(), order);
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

        orderLogger.logAddPancake(order, pancake.description(), pancakes);
    }

    public void removePancakes(String description, UUID orderId, int count) {
        final AtomicInteger removedCount = new AtomicInteger(0);
        pancakes.removeIf(pancake -> {
            return pancake.getOrderId().equals(orderId) &&
                    pancake.description().equals(description) &&
                    removedCount.getAndIncrement() < count;
        });

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
        orderLogger.logRemovePancakes(order, description, removedCount.get(), pancakes);
    }

    public void cancelOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
        orderLogger.logCancelOrder(order, this.pancakes);

        pancakes.removeIf(pancake -> pancake.getOrderId().equals(orderId));
        orderRepository.deleteById(orderId);
        orderStateService.remove(orderId);

        orderLogger.logCancelOrder(order, pancakes);
    }

    public void completeOrder(UUID orderId) {
        orderStateService.update(orderId, OrderState.COMPLETED);
    }

    public Set<UUID> listCompletedOrders() {
        return orderStateService.getOrderIdsByState(OrderState.COMPLETED);

    }

    public void prepareOrder(UUID orderId) {
        orderStateService.update(orderId, OrderState.PREPARED);
    }

    public Set<UUID> listPreparedOrders() {
        return orderStateService.getOrderIdsByState(OrderState.PREPARED);
    }

    public Object[] deliverOrder(UUID orderId) {
        if (orderStateService.get(orderId) != OrderState.PREPARED) return null;

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
        List<String> pancakesToDeliver = viewOrder(orderId);
        orderLogger.logDeliverOrder(order, this.pancakes);

        pancakes.removeIf(pancake -> pancake.getOrderId().equals(orderId));
        orderRepository.deleteById(orderId);
        orderStateService.remove(orderId);

        return new Object[]{order, pancakesToDeliver};
    }
}
