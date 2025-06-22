package org.pancakelab.model.pancakes;


import java.util.List;

public class DefaultPancakeFactory implements PancakeFactory {
    @Override
    public PancakeRecipe createRecipe(List<Ingredient> ingredients) {
        return new Pancake.Builder()
                .withIngredients(ingredients)
                .build();
    }
}