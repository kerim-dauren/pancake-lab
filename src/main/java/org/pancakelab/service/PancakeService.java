package org.pancakelab.service;

import org.pancakelab.exception.OrderNotFoundException;
import org.pancakelab.model.orders.DefaultOrderFactory;
import org.pancakelab.model.orders.Order;
import org.pancakelab.model.orders.OrderState;
import org.pancakelab.model.pancakes.Ingredient;
import org.pancakelab.model.pancakes.PancakeFactory;
import org.pancakelab.model.pancakes.PancakeRecipe;
import org.pancakelab.repository.OrderRepository;
import org.pancakelab.repository.PancakeRepository;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Service class for managing pancake orders.
 * This class provides methods to create, modify, and manage pancake orders,
 * including adding pancakes, viewing orders, and changing order states.
 */
public class PancakeService {
    private final OrderRepository orderRepository;
    private final DefaultOrderFactory orderFactory;
    private final OrderLogger orderLogger;
    private final OrderStateService orderStateService;
    private final PancakeRepository pancakeRepository;
    private final PancakeFactory pancakeFactory;

    public PancakeService(
            OrderRepository orderRepository,
            DefaultOrderFactory orderFactory,
            OrderLogger orderLogger,
            OrderStateService orderStateService,
            PancakeRepository pancakeRepository,
            PancakeFactory pancakeFactory
    ) {
        this.orderRepository = orderRepository;
        this.orderFactory = orderFactory;
        this.orderLogger = orderLogger;
        this.orderStateService = orderStateService;
        this.pancakeRepository = pancakeRepository;
        this.pancakeFactory = pancakeFactory;
    }

    private final Map<UUID, Lock> orderLocks = new ConcurrentHashMap<>();

    private Lock getOrderLock(UUID orderId) {
        return orderLocks.computeIfAbsent(orderId, id -> new ReentrantLock());
    }

    public Order createOrder(int building, int room) {
        Order order = orderFactory.createOrder(building, room);
        orderRepository.save(order);
        orderStateService.update(order.getId(), OrderState.CREATED);
        return order;
    }

    @Deprecated
    // This method is deprecated and will be removed in future versions.
    // Use addPancake instead.
    public void addDarkChocolatePancake(UUID orderId, int count) {
        withOrderLock(orderId, () -> {
            Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
            for (int i = 0; i < count; ++i) {
                addPancake(pancakeFactory.createRecipe(List.of(Ingredient.DARK_CHOCOLATE)), order);
            }
        });
    }

    @Deprecated
    // This method is deprecated and will be removed in future versions.
    // Use addPancake instead.
    public void addDarkChocolateWhippedCreamPancake(UUID orderId, int count) {
        withOrderLock(orderId, () -> {
            Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
            for (int i = 0; i < count; ++i) {
                addPancake(pancakeFactory.createRecipe(List.of(Ingredient.DARK_CHOCOLATE, Ingredient.WHIPPED_CREAM)), order);
            }
        });
    }

    @Deprecated
    // This method is deprecated and will be removed in future versions.
    // Use addPancake instead.
    public void addDarkChocolateWhippedCreamHazelnutsPancake(UUID orderId, int count) {
        withOrderLock(orderId, () -> {
            Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
            for (int i = 0; i < count; ++i) {
                addPancake(pancakeFactory.createRecipe(List.of(Ingredient.DARK_CHOCOLATE, Ingredient.WHIPPED_CREAM, Ingredient.HAZELNUTS)), order);
            }
        });
    }

    @Deprecated
    // This method is deprecated and will be removed in future versions.
    // Use addPancake instead.
    public void addMilkChocolatePancake(UUID orderId, int count) {
        withOrderLock(orderId, () -> {
            Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
            for (int i = 0; i < count; ++i) {
                addPancake(pancakeFactory.createRecipe(List.of(Ingredient.MILK_CHOCOLATE)), order);
            }
        });
    }

    @Deprecated
    // This method is deprecated and will be removed in future versions.
    // Use addPancake instead.
    public void addMilkChocolateHazelnutsPancake(UUID orderId, int count) {
        withOrderLock(orderId, () -> {
            Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
            for (int i = 0; i < count; ++i) {
                addPancake(pancakeFactory.createRecipe(List.of(Ingredient.MILK_CHOCOLATE, Ingredient.HAZELNUTS)), order);
            }
        });
    }

    /**
     * Adds a pancake to the specified order.
     *
     * @param orderId the ID of the order to which the pancake will be added
     * @param pancake the pancake recipe to be added
     * @param count   the number of pancakes to add
     */
    public void addPancake(UUID orderId, PancakeRecipe pancake, int count) {
        withOrderLock(orderId, () -> {
            Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
            for (int i = 0; i < count; ++i) {
                addPancake(pancake, order);
            }
        });
    }

    public List<String> viewOrder(UUID orderId) {
        return pancakeRepository.viewOrderPancakes(orderId);
    }

    private void addPancake(PancakeRecipe pancake, Order order) {
        int pancakeCount = pancakeRepository.addPancake(order.getId(), pancake);
        orderLogger.logAddPancake(order, pancake.description(), pancakeCount);
    }

    public void removePancakes(String description, UUID orderId, int count) {
        withOrderLock(orderId, () -> {
            int removedCount = pancakeRepository.removePancakes(orderId, description, count);
            Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
            orderLogger.logRemovePancakes(order, description, pancakeRepository.getPancakesCount(order.getId()), removedCount);
        });
    }

    public void cancelOrder(UUID orderId) {
        withOrderLock(orderId, () -> {
            Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
            orderLogger.logCancelOrder(order, pancakeRepository.getPancakesCount(order.getId()));
            pancakeRepository.remove(orderId);
            orderRepository.deleteById(orderId);
            orderStateService.remove(orderId);
        });
        removeOrderLock(orderId);
    }

    public void completeOrder(UUID orderId) {
        withOrderLock(orderId, () -> orderStateService.update(orderId, OrderState.COMPLETED));
    }

    public Set<UUID> listCompletedOrders() {
        return orderStateService.getOrderIdsByState(OrderState.COMPLETED);

    }

    public void prepareOrder(UUID orderId) {
        withOrderLock(orderId, () -> orderStateService.update(orderId, OrderState.PREPARED));
    }

    public Set<UUID> listPreparedOrders() {
        return orderStateService.getOrderIdsByState(OrderState.PREPARED);
    }

    public Object[] deliverOrder(UUID orderId) {
        final Lock lock = getOrderLock(orderId);
        lock.lock();

        try {
            if (orderStateService.get(orderId) != OrderState.PREPARED) return null;

            Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
            List<String> pancakesToDeliver = viewOrder(orderId);
            orderLogger.logDeliverOrder(order, pancakeRepository.getPancakesCount(order.getId()));
            pancakeRepository.remove(orderId);
            orderRepository.deleteById(orderId);
            orderStateService.remove(orderId);

            return new Object[]{order, pancakesToDeliver};
        } finally {
            lock.unlock();
            removeOrderLock(orderId);
        }
    }

    private void withOrderLock(UUID orderId, Runnable action) {
        final Lock lock = getOrderLock(orderId);
        lock.lock();
        try {
            action.run();
        } finally {
            lock.unlock();
        }
    }

    private void removeOrderLock(UUID orderId) {
        orderLocks.remove(orderId);
    }
}
