package org.pancakelab.service;

import org.pancakelab.model.Order;
import org.pancakelab.model.pancakes.PancakeRecipe;

import java.util.List;

public interface OrderLogger {
    void logAddPancake(Order order, String description, List<PancakeRecipe> pancakes);
    void logRemovePancakes(Order order, String description, int count, List<PancakeRecipe> pancakes);
    void logCancelOrder(Order order, List<PancakeRecipe> pancakes);
    void logDeliverOrder(Order order, List<PancakeRecipe> pancakes);
}
