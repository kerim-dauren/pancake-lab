package org.pancakelab.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pancakelab.model.orders.DefaultOrderFactory;
import org.pancakelab.model.orders.Order;
import org.pancakelab.model.orders.OrderValidator;
import org.pancakelab.model.orders.OrderValidatorConfig;
import org.pancakelab.model.pancakes.DefaultPancakeFactory;
import org.pancakelab.model.pancakes.Ingredient;
import org.pancakelab.model.pancakes.PancakeFactory;
import org.pancakelab.model.pancakes.PancakeRecipe;
import org.pancakelab.repository.impl.InMemoryOrderRepository;
import org.pancakelab.repository.impl.InMemoryPancakeRepository;
import org.pancakelab.service.impl.InMemoryOrderStateService;
import org.pancakelab.service.impl.NoLogOrderLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PancakeServiceConcurrencyTest {

    private PancakeService pancakeService;
    private PancakeFactory pancakeFactory;

    private UUID orderId;

    @BeforeEach
    void setup() {
        // Use real concurrent repository implementations or proper mocks
        var orderRepository = new InMemoryOrderRepository();
        var pancakeRepository = new InMemoryPancakeRepository();

        var orderValidatorConfig = new OrderValidatorConfig(1, 10, 1, 1000);
        var orderValidator = new OrderValidator(orderValidatorConfig);
        var orderFactory = new DefaultOrderFactory(orderValidator);
        var orderStateService = new InMemoryOrderStateService();
        var orderLogger = new NoLogOrderLogger();
        pancakeFactory = new DefaultPancakeFactory();

        pancakeService = new PancakeService(
                orderRepository,
                orderFactory,
                orderLogger,
                orderStateService,
                pancakeRepository,
                pancakeFactory
        );

        Order order = pancakeService.createOrder(1, 101);
        orderId = order.getId();
    }

    @Test
    void GivenMultipleThreads_WhenAddingPancakesToSameOrder_ThenNoPancakeIsLost_Test() throws InterruptedException {
        int threads = 10;
        int pancakesPerThread = 100;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(threads);

        PancakeRecipe recipe = pancakeFactory.createRecipe(List.of(Ingredient.DARK_CHOCOLATE));

        for (int i = 0; i < threads; i++) {
            executor.submit(() -> {
                for (int j = 0; j < pancakesPerThread; j++) {
                    pancakeService.addPancake(orderId, recipe, 1);
                }
                latch.countDown();
            });
        }

        latch.await();
        executor.shutdown();

        List<String> pancakes = pancakeService.viewOrder(orderId);
        assertEquals(threads * pancakesPerThread, pancakes.size(),
                "All pancakes should have been added without lost updates.");
    }

    @Test
    void GivenConcurrentAddAndRemove_WhenOperatingOnSameOrder_ThenFinalPancakeCountIsConsistent_Test() throws InterruptedException {
        int threads = 5;
        int pancakesPerThread = 50;
        ExecutorService executor = Executors.newFixedThreadPool(threads * 2);
        CountDownLatch latch = new CountDownLatch(threads * 2);

        PancakeRecipe recipe = pancakeFactory.createRecipe(List.of(Ingredient.MILK_CHOCOLATE));

        // First, add initial pancakes
        pancakeService.addPancake(orderId, recipe, threads * pancakesPerThread);

        // Concurrent remove and add
        for (int i = 0; i < threads; i++) {
            executor.submit(() -> {
                for (int j = 0; j < pancakesPerThread; j++) {
                    pancakeService.addPancake(orderId, recipe, 1);
                }
                latch.countDown();
            });
            executor.submit(() -> {
                for (int j = 0; j < pancakesPerThread; j++) {
                    pancakeService.removePancakes(recipe.description(), orderId, 1);
                }
                latch.countDown();
            });
        }

        latch.await();
        executor.shutdown();

        List<String> pancakes = pancakeService.viewOrder(orderId);
        // Net change should be zero
        assertEquals(threads * pancakesPerThread, pancakes.size(),
                "Pancake count should remain as initial after concurrent add and remove.");
    }

    @Test
    void GivenConcurrentAddToDifferentOrders_WhenExecutedInParallel_ThenEachOrderHasCorrectPancakeCount_Test() throws InterruptedException {
        int orderCount = 5;
        int pancakesPerOrder = 100;
        List<UUID> orderIds = new ArrayList<>();
        for (int i = 0; i < orderCount; i++) {
            orderIds.add(pancakeService.createOrder(1, 200 + i).getId());
        }

        PancakeRecipe recipe = pancakeFactory.createRecipe(List.of(Ingredient.WHIPPED_CREAM));
        ExecutorService executor = Executors.newFixedThreadPool(orderCount);
        CountDownLatch latch = new CountDownLatch(orderCount);

        for (UUID oid : orderIds) {
            executor.submit(() -> {
                for (int j = 0; j < pancakesPerOrder; j++) {
                    pancakeService.addPancake(oid, recipe, 1);
                }
                latch.countDown();
            });
        }

        latch.await();
        executor.shutdown();

        for (UUID oid : orderIds) {
            List<String> pancakes = pancakeService.viewOrder(oid);
            assertEquals(pancakesPerOrder, pancakes.size(),
                    "Each order should have the correct number of pancakes after parallel execution.");
        }
    }
}