package org.pancakelab.model.pancakes;

import java.util.List;

/**
 * Represents a pancake recipe in the Pancake Lab.
 * Each recipe consists of a list of ingredients and provides a description of the pancake.
 */
public interface PancakeRecipe {
    default String description() {
        return "Delicious pancake with %s!".formatted(String.join(", ", ingredients().stream().map(Ingredient::getDescription).toList()));
    }

    List<Ingredient> ingredients();
}
