package org.pancakelab.model.pancakes;

import java.util.List;

/**
 * Factory for creating PancakeRecipe instances from a list of ingredients.
 */
public interface PancakeFactory {
    /**
     * Builds a PancakeRecipe given the specified ingredients.
     *
     * @param ingredients List of ingredients
     * @return new PancakeRecipe
     */
    PancakeRecipe createRecipe(List<Ingredient> ingredients);
}
