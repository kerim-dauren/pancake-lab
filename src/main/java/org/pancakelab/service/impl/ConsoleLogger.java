package org.pancakelab.service.impl;

import org.pancakelab.model.Order;
import org.pancakelab.service.OrderLogger;

public class ConsoleLogger implements OrderLogger {

    public void logAddPancake(Order order, String description, int pancakeCount) {
        String log = "Added pancake with description '%s' ".formatted(description) +
                "to order %s containing %d pancakes, ".formatted(order.getId(), pancakeCount) +
                "for building %d, room %d.".formatted(order.getBuilding(), order.getRoom());

        System.out.println(log);
    }

    public void logRemovePancakes(Order order, String description, int pancakesInOrder, int removedCount) {

        String log = "Removed %d pancake(s) with description '%s' ".formatted(removedCount, description) +
                "from order %s now containing %d pancakes, ".formatted(order.getId(), pancakesInOrder) +
                "for building %d, room %d.".formatted(order.getBuilding(), order.getRoom());

        System.out.println(log);
    }

    public void logCancelOrder(Order order, int pancakesInOrder) {
        String log = "Cancelled order %s with %d pancakes ".formatted(order.getId(), pancakesInOrder) +
                "for building %d, room %d.".formatted(order.getBuilding(), order.getRoom());

        System.out.println(log);
    }

    public void logDeliverOrder(Order order, int pancakesInOrder) {
        String log = "Order %s with %d pancakes ".formatted(order.getId(), pancakesInOrder) +
                "for building %d, room %d out for delivery.".formatted(order.getBuilding(), order.getRoom());

        System.out.println(log);
    }
}
