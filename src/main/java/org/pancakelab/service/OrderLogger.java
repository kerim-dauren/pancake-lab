package org.pancakelab.service;

import org.pancakelab.model.Order;

/**
 * Interface for logging order-related events.
 * Implementations of this interface can log events to various outputs, such as console, files, or external systems.
 */
public interface OrderLogger {
    /**
     * Logs the addition of a pancake to an order.
     *
     * @param order        the order to which the pancake is added
     * @param description  a description of the pancake
     * @param pancakeCount the total number of pancakes in the order after addition
     */
    void logAddPancake(Order order, String description, int pancakeCount);

    /**
     * Logs the removal of pancakes from an order.
     *
     * @param order           the order from which pancakes are removed
     * @param description     a description of the pancakes being removed
     * @param pancakesInOrder the total number of pancakes in the order before removal
     * @param removedCount    the number of pancakes removed
     */
    void logRemovePancakes(Order order, String description, int pancakesInOrder, int removedCount);

    /**
     * Logs the cancellation of an order.
     *
     * @param order           the order that is being cancelled
     * @param pancakesInOrder the total number of pancakes in the order at the time of cancellation
     */
    void logCancelOrder(Order order, int pancakesInOrder);

    /**
     * Logs the delivery of an order.
     *
     * @param order           the order that is being delivered
     * @param pancakesInOrder the total number of pancakes in the order at the time of delivery
     */
    void logDeliverOrder(Order order, int pancakesInOrder);
}
