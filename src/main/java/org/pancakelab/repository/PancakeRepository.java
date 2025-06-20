package org.pancakelab.repository;

import org.pancakelab.model.pancakes.PancakeRecipe;

import java.util.List;
import java.util.UUID;

public interface PancakeRepository {

    List<String> viewOrderPancakes(UUID orderId);

    int addPancake(UUID orderId, PancakeRecipe pancakeRecipe);

    void remove(UUID orderId);

    int removePancakes(UUID orderId, String description, int count);

    int getPancakesCount(UUID orderId);

}
