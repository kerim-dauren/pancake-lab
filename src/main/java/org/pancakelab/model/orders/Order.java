package org.pancakelab.model.orders;

import java.util.Objects;
import java.util.UUID;

/**
 * Represents an order in the Pancake Lab.
 * Each order has a unique ID, a building number, and a room number.
 * The ID is generated randomly upon creation of the order.
 */
public class Order {
    private final UUID id;
    private final int building;
    private final int room;

    Order(int building, int room) {
        this.id = UUID.randomUUID();
        this.building = building;
        this.room = room;
    }

    public UUID getId() {
        return id;
    }

    public int getBuilding() {
        return building;
    }

    public int getRoom() {
        return room;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
