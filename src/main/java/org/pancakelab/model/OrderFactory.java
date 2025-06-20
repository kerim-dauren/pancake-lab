package org.pancakelab.model;

public class OrderFactory {

    private final OrderValidator orderValidator;

    public OrderFactory(OrderValidator orderValidator) {
        this.orderValidator = orderValidator;
    }

    public Order createOrder(int building, int room) {
        orderValidator.validate(building, room);
        return new Order(building, room);
    }
}
