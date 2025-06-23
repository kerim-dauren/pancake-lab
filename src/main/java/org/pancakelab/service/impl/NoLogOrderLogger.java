package org.pancakelab.service.impl;

import org.pancakelab.model.orders.Order;
import org.pancakelab.service.OrderLogger;

public class NoLogOrderLogger implements OrderLogger {
    @Override
    public void logAddPancake(Order order, String description, int pancakeCount) {

    }

    @Override
    public void logRemovePancakes(Order order, String description, int pancakesInOrder, int removedCount) {

    }

    @Override
    public void logCancelOrder(Order order, int pancakesInOrder) {

    }

    @Override
    public void logDeliverOrder(Order order, int pancakesInOrder) {

    }
}
