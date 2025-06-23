package org.pancakelab.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pancakelab.exception.ValidationException;
import org.pancakelab.model.orders.*;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {
    private static final int MIN_BUILDING = 1;
    private static final int MAX_BUILDING = 10;
    private static final int MIN_ROOM = 1;
    private static final int MAX_ROOM = 1000;

    private OrderFactory orderFactory;

    @BeforeEach
    public void setUp() {
        var orderValidatorConfig = new OrderValidatorConfig(MIN_BUILDING, MAX_BUILDING, MIN_ROOM, MAX_ROOM);
        orderFactory = new DefaultOrderFactory(new OrderValidator(orderValidatorConfig));
    }

    @Test
    public void GivenOrderDoesNotExist_WhenCreatingOrder_ThenOrderCreated_Test() {
        Order order = orderFactory.createOrder(MIN_BUILDING, MIN_ROOM + 1);
        assertNotNull(order);
        assertEquals(MIN_BUILDING, order.getBuilding());
        assertEquals(MIN_ROOM + 1, order.getRoom());
    }

    @Test
    public void GivenOrderExists_WhenCreatingOrderWithSameDetails_ThenOrderCreated_Test() {
        Order order1 = orderFactory.createOrder(MIN_BUILDING + 1, MAX_ROOM - 1);
        Order order2 = orderFactory.createOrder(MIN_BUILDING + 1, MAX_ROOM - 1);
        assertNotNull(order1);
        assertNotNull(order2);
        assertEquals(order1.getBuilding(), order2.getBuilding());
        assertEquals(order1.getRoom(), order2.getRoom());
    }

    @Test
    public void GivenOrderDoesNotExist_WhenCreatingOrderWithInvalidParams_ThenThrowsValidationException_Test() {
        assertThrows(ValidationException.class, () -> orderFactory.createOrder(MIN_BUILDING - 1, MIN_ROOM));
        assertThrows(ValidationException.class, () -> orderFactory.createOrder(MAX_BUILDING + 1, MIN_ROOM));
        assertThrows(ValidationException.class, () -> orderFactory.createOrder(MIN_BUILDING, MIN_ROOM - 1));
        assertThrows(ValidationException.class, () -> orderFactory.createOrder(MIN_BUILDING, MAX_ROOM + 1));
    }
}
