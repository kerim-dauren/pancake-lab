package org.pancakelab.service;

import org.pancakelab.exception.OrderNotFoundException;
import org.pancakelab.model.Order;
import org.pancakelab.model.OrderFactory;
import org.pancakelab.model.OrderState;
import org.pancakelab.model.pancakes.Ingredient;
import org.pancakelab.model.pancakes.Pancake;
import org.pancakelab.model.pancakes.PancakeRecipe;
import org.pancakelab.repository.OrderRepository;
import org.pancakelab.repository.PancakeRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class PancakeService {
    private final OrderRepository orderRepository;
    private final OrderFactory orderFactory;
    private final OrderLogger orderLogger;
    private final OrderStateService orderStateService;
    private final PancakeRepository pancakeRepository;

    public PancakeService(
            OrderRepository orderRepository,
            OrderFactory orderFactory,
            OrderLogger orderLogger,
            OrderStateService orderStateService,
            PancakeRepository pancakeRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderFactory = orderFactory;
        this.orderLogger = orderLogger;
        this.orderStateService = orderStateService;
        this.pancakeRepository = pancakeRepository;
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
                    .withIngredients(List.of(Ingredient.DARK_CHOCOLATE))
                    .build(), order);
        }
    }

    public void addDarkChocolateWhippedCreamPancake(UUID orderId, int count) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
        for (int i = 0; i < count; ++i) {
            addPancake(new Pancake.Builder()
                    .withIngredients(List.of(Ingredient.DARK_CHOCOLATE, Ingredient.WHIPPED_CREAM))
                    .build(), order);
        }
    }

    public void addDarkChocolateWhippedCreamHazelnutsPancake(UUID orderId, int count) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
        for (int i = 0; i < count; ++i) {
            addPancake(new Pancake.Builder()
                    .withIngredients(List.of(Ingredient.DARK_CHOCOLATE, Ingredient.WHIPPED_CREAM, Ingredient.HAZELNUTS))
                    .build(), order);
        }
    }

    public void addMilkChocolatePancake(UUID orderId, int count) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
        for (int i = 0; i < count; ++i) {
            addPancake(new Pancake.Builder()
                    .withIngredients(List.of(Ingredient.MILK_CHOCOLATE))
                    .build(), order);
        }
    }

    public void addMilkChocolateHazelnutsPancake(UUID orderId, int count) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
        for (int i = 0; i < count; ++i) {
            addPancake(new Pancake.Builder()
                    .withIngredients(List.of(Ingredient.MILK_CHOCOLATE, Ingredient.HAZELNUTS))
                    .build(), order);
        }
    }

    public List<String> viewOrder(UUID orderId) {
        return pancakeRepository.viewOrderPancakes(orderId);
    }

    private void addPancake(PancakeRecipe pancake, Order order) {
        int pancakeCount = pancakeRepository.addPancake(order.getId(), pancake);
        orderLogger.logAddPancake(order, pancake.description(), pancakeCount);
    }

    public void removePancakes(String description, UUID orderId, int count) {
        int removedCount = pancakeRepository.removePancakes(orderId, description, count);

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
        orderLogger.logRemovePancakes(order, description, pancakeRepository.getPancakesCount(order.getId()), removedCount);
    }

    public void cancelOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
        orderLogger.logCancelOrder(order, pancakeRepository.getPancakesCount(order.getId()));
        pancakeRepository.remove(orderId);
        orderRepository.deleteById(orderId);
        orderStateService.remove(orderId);

        orderLogger.logCancelOrder(order, pancakeRepository.getPancakesCount(order.getId()));
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
        orderLogger.logDeliverOrder(order, pancakeRepository.getPancakesCount(order.getId()));
        pancakeRepository.remove(orderId);
        orderRepository.deleteById(orderId);
        orderStateService.remove(orderId);

        return new Object[]{order, pancakesToDeliver};
    }
}
