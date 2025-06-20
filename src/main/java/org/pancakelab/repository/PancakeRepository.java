package org.pancakelab.repository;

import org.pancakelab.model.pancakes.PancakeRecipe;

import java.util.List;
import java.util.UUID;

/**
 * Interface for managing pancake orders in a repository.
 * Provides methods to view, add, and remove pancakes associated with an order.
 */
public interface PancakeRepository {

    /**
     * Retrieves a list of pancake descriptions for a given order.
     *
     * @param orderId the ID of the order
     * @return a list of pancake descriptions
     */
    List<String> viewOrderPancakes(UUID orderId);

    /**
     * Adds a pancake to an order.
     *
     * @param orderId       the ID of the order
     * @param pancakeRecipe the recipe of the pancake to add
     * @return the total number of pancakes in the order after addition
     */
    int addPancake(UUID orderId, PancakeRecipe pancakeRecipe);

    /**
     * Removes all pancakes associated with a specific order.
     *
     * @param orderId the ID of the order to remove
     */
    void remove(UUID orderId);

    /**
     * Removes a specified number of pancakes with a given description from an order.
     *
     * @param orderId     the ID of the order
     * @param description the description of the pancakes to remove
     * @param count       the number of pancakes to remove
     * @return the number of pancakes removed
     */
    int removePancakes(UUID orderId, String description, int count);

    /**
     * Retrieves the total count of pancakes in a specific order.
     *
     * @param orderId the ID of the order
     * @return the total number of pancakes in the order
     */
    int getPancakesCount(UUID orderId);

}
