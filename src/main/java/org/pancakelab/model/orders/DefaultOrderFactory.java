package org.pancakelab.model.orders;

/**
 * Factory class for creating Order instances.
 * It uses an OrderValidator to ensure that the order details are valid before creation.
 */
public class DefaultOrderFactory implements OrderFactory {

    private final OrderValidator orderValidator;

    public DefaultOrderFactory(OrderValidator orderValidator) {
        this.orderValidator = orderValidator;
    }

    public Order createOrder(int building, int room) {
        orderValidator.validate(building, room);
        return new Order(building, room);
    }
}
