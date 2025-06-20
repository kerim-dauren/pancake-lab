package org.pancakelab.service;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.pancakelab.exception.OrderNotFoundException;
import org.pancakelab.exception.ValidationException;
import org.pancakelab.model.Order;
import org.pancakelab.model.OrderFactory;
import org.pancakelab.model.OrderValidator;
import org.pancakelab.model.OrderValidatorConfig;
import org.pancakelab.repository.OrderRepository;
import org.pancakelab.repository.impl.InMemoryOrderRepository;
import org.pancakelab.service.impl.ConsoleLogger;
import org.pancakelab.service.impl.InMemoryOrderStateService;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PancakeServiceTest {
    private static final int MIN_BUILDING = 1;
    private static final int MAX_BUILDING = 10;
    private static final int MIN_ROOM = 1;
    private static final int MAX_ROOM = 100;

    private final OrderValidatorConfig orderValidatorConfig = new OrderValidatorConfig(MIN_BUILDING, MAX_BUILDING, MIN_ROOM, MAX_ROOM);
    private final OrderValidator orderValidator = new OrderValidator(orderValidatorConfig);
    private final OrderFactory orderFactory = new OrderFactory(orderValidator);
    private final OrderRepository orderRepository = new InMemoryOrderRepository();
    private final OrderLogger orderLogger = new ConsoleLogger();
    private final OrderStateService orderStateService = new InMemoryOrderStateService();

    private final PancakeService pancakeService = new PancakeService(
            orderRepository,
            orderFactory,
            orderLogger,
            orderStateService
    );

    private Order order = null;

    private final static String DARK_CHOCOLATE_PANCAKE_DESCRIPTION = "Delicious pancake with dark chocolate!";
    private final static String MILK_CHOCOLATE_PANCAKE_DESCRIPTION = "Delicious pancake with milk chocolate!";
    private final static String MILK_CHOCOLATE_HAZELNUTS_PANCAKE_DESCRIPTION = "Delicious pancake with milk chocolate, hazelnuts!";

    @Test
    @org.junit.jupiter.api.Order(10)
    public void GivenOrderDoesNotExist_WhenCreatingOrder_ThenOrderCreatedWithCorrectData_Test() {
        // setup

        // exercise
        order = pancakeService.createOrder(MIN_BUILDING, MIN_ROOM);

        assertEquals(MIN_BUILDING, order.getBuilding());
        assertEquals(MIN_ROOM, order.getRoom());

        // verify

        // tear down
    }

    @Test
    @org.junit.jupiter.api.Order(11)
    public void GivenOrderDoesNotExist_WhenCreatingOrderWithInvalidParams_ThenThrowsValidationException_Test() {
        assertThrows(ValidationException.class, () -> pancakeService.createOrder(MIN_BUILDING - 1, MIN_ROOM));
        assertThrows(ValidationException.class, () -> pancakeService.createOrder(MAX_BUILDING + 1, MIN_ROOM));
        assertThrows(ValidationException.class, () -> pancakeService.createOrder(MIN_BUILDING, MIN_ROOM - 1));
        assertThrows(ValidationException.class, () -> pancakeService.createOrder(MIN_BUILDING, MAX_ROOM + 1));
    }

    @Test
    @org.junit.jupiter.api.Order(20)
    public void GivenOrderExists_WhenAddingPancakes_ThenCorrectNumberOfPancakesAdded_Test() {
        // setup

        // exercise
        addPancakes();

        // verify
        List<String> ordersPancakes = pancakeService.viewOrder(order.getId());

        assertEquals(List.of(DARK_CHOCOLATE_PANCAKE_DESCRIPTION,
                DARK_CHOCOLATE_PANCAKE_DESCRIPTION,
                DARK_CHOCOLATE_PANCAKE_DESCRIPTION,
                MILK_CHOCOLATE_PANCAKE_DESCRIPTION,
                MILK_CHOCOLATE_PANCAKE_DESCRIPTION,
                MILK_CHOCOLATE_PANCAKE_DESCRIPTION,
                MILK_CHOCOLATE_HAZELNUTS_PANCAKE_DESCRIPTION,
                MILK_CHOCOLATE_HAZELNUTS_PANCAKE_DESCRIPTION,
                MILK_CHOCOLATE_HAZELNUTS_PANCAKE_DESCRIPTION), ordersPancakes);

        // tear down
    }

    @Test
    @org.junit.jupiter.api.Order(21)
    public void GivenOrderDoesNotExist_WhenViewOrderWithInvalidOrderId_ThenThrowsOrderNotFoundException_Test() {
        UUID orderId = UUID.randomUUID();
        assertThrows(OrderNotFoundException.class, () -> pancakeService.addDarkChocolatePancake(orderId, 1));
        assertThrows(OrderNotFoundException.class, () -> pancakeService.addDarkChocolateWhippedCreamPancake(orderId, 1));
        assertThrows(OrderNotFoundException.class, () -> pancakeService.addDarkChocolateWhippedCreamHazelnutsPancake(orderId, 1));
        assertThrows(OrderNotFoundException.class, () -> pancakeService.addMilkChocolatePancake(orderId, 1));
        assertThrows(OrderNotFoundException.class, () -> pancakeService.addMilkChocolateHazelnutsPancake(orderId, 1));
    }

    @Test
    @org.junit.jupiter.api.Order(30)
    public void GivenPancakesExists_WhenRemovingPancakes_ThenCorrectNumberOfPancakesRemoved_Test() {
        // setup

        // exercise
        pancakeService.removePancakes(DARK_CHOCOLATE_PANCAKE_DESCRIPTION, order.getId(), 2);
        pancakeService.removePancakes(MILK_CHOCOLATE_PANCAKE_DESCRIPTION, order.getId(), 3);
        pancakeService.removePancakes(MILK_CHOCOLATE_HAZELNUTS_PANCAKE_DESCRIPTION, order.getId(), 1);

        // verify
        List<String> ordersPancakes = pancakeService.viewOrder(order.getId());

        assertEquals(List.of(DARK_CHOCOLATE_PANCAKE_DESCRIPTION,
                MILK_CHOCOLATE_HAZELNUTS_PANCAKE_DESCRIPTION,
                MILK_CHOCOLATE_HAZELNUTS_PANCAKE_DESCRIPTION), ordersPancakes);

        // tear down
    }

    @Test
    @org.junit.jupiter.api.Order(40)
    public void GivenOrderExists_WhenCompletingOrder_ThenOrderCompleted_Test() {
        // setup

        // exercise
        pancakeService.completeOrder(order.getId());

        // verify
        Set<UUID> completedOrdersOrders = pancakeService.listCompletedOrders();
        assertTrue(completedOrdersOrders.contains(order.getId()));

        // tear down
    }

    @Test
    @org.junit.jupiter.api.Order(50)
    public void GivenOrderExists_WhenPreparingOrder_ThenOrderPrepared_Test() {
        // setup

        // exercise
        pancakeService.prepareOrder(order.getId());

        // verify
        Set<UUID> completedOrders = pancakeService.listCompletedOrders();
        assertFalse(completedOrders.contains(order.getId()));

        Set<UUID> preparedOrders = pancakeService.listPreparedOrders();
        assertTrue(preparedOrders.contains(order.getId()));

        // tear down
    }

    @Test
    @org.junit.jupiter.api.Order(60)
    public void GivenOrderExists_WhenDeliveringOrder_ThenCorrectOrderReturnedAndOrderRemovedFromTheDatabase_Test() {
        // setup
        List<String> pancakesToDeliver = pancakeService.viewOrder(order.getId());

        // exercise
        Object[] deliveredOrder = pancakeService.deliverOrder(order.getId());

        // verify
        Set<UUID> completedOrders = pancakeService.listCompletedOrders();
        assertFalse(completedOrders.contains(order.getId()));

        Set<UUID> preparedOrders = pancakeService.listPreparedOrders();
        assertFalse(preparedOrders.contains(order.getId()));

        List<String> ordersPancakes = pancakeService.viewOrder(order.getId());

        assertEquals(List.of(), ordersPancakes);
        assertEquals(order.getId(), ((Order) deliveredOrder[0]).getId());
        assertEquals(pancakesToDeliver, (List<String>) deliveredOrder[1]);

        // tear down
        order = null;
    }

    @Test
    @org.junit.jupiter.api.Order(70)
    public void GivenOrderExists_WhenCancellingOrder_ThenOrderAndPancakesRemoved_Test() {
        // setup
        order = pancakeService.createOrder(MIN_BUILDING, MIN_ROOM);
        addPancakes();

        // exercise
        pancakeService.cancelOrder(order.getId());

        // verify
        Set<UUID> completedOrders = pancakeService.listCompletedOrders();
        assertFalse(completedOrders.contains(order.getId()));

        Set<UUID> preparedOrders = pancakeService.listPreparedOrders();
        assertFalse(preparedOrders.contains(order.getId()));

        List<String> ordersPancakes = pancakeService.viewOrder(order.getId());

        assertEquals(List.of(), ordersPancakes);

        // tear down
    }

    private void addPancakes() {
        pancakeService.addDarkChocolatePancake(order.getId(), 3);
        pancakeService.addMilkChocolatePancake(order.getId(), 3);
        pancakeService.addMilkChocolateHazelnutsPancake(order.getId(), 3);
    }
}
