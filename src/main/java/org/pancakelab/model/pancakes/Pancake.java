package org.pancakelab.model.pancakes;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a pancake in the Pancake Lab.
 * Each pancake is defined by its ingredients and can be built using the Builder pattern.
 */
public class Pancake implements PancakeRecipe {
    private List<Ingredient> ingredients;

    // Private constructor to prevent instantiation without ingredients
    private Pancake() {
    }

    private Pancake(List<Ingredient> ingredients) {
        this.ingredients = List.copyOf(ingredients); // Ensure immutability
    }

    @Override
    public List<Ingredient> ingredients() {
        return List.copyOf(ingredients);
    }

    public static class Builder {
        private final List<Ingredient> ingredients;

        public Builder() {
            ingredients = new ArrayList<>();
        }

        public Builder withIngredients(List<Ingredient> ingredients) {
            this.ingredients.addAll(ingredients);
            return this;
        }

        public Pancake build() {
            if (ingredients.isEmpty()) {
                throw new IllegalStateException("Ingredients must be provided");
            }
            return new Pancake(ingredients);
        }
    }
}
