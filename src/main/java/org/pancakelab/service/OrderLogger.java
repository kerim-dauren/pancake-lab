package org.pancakelab.service;

import org.pancakelab.model.Order;

public interface OrderLogger {
    void logAddPancake(Order order, String description, int pancakeCount);

    void logRemovePancakes(Order order, String description, int pancakesInOrder, int removedCount);

    void logCancelOrder(Order order, int pancakesInOrder);

    void logDeliverOrder(Order order, int pancakesInOrder);
}
