package org.pancakelab.model.pancakes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Pancake implements PancakeRecipe {
    private UUID orderId;
    private List<String> ingredients;

    // Private constructor to prevent instantiation without ingredients
    private Pancake() {
    }

    private Pancake(List<String> ingredients) {
        this.ingredients = List.copyOf(ingredients); // Ensure immutability
    }


    @Override
    public UUID getOrderId() {
        return orderId;
    }

    @Override
    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    @Override
    public List<String> ingredients() {
        return ingredients;
    }

    public static class Builder {
        private final List<String> ingredients;

        public Builder() {
            ingredients = new ArrayList<>();
        }

        public Builder withIngredients(List<String> ingredients) {
            this.ingredients.addAll(ingredients);
            return this;
        }

        public Pancake build() {
            if (ingredients == null || ingredients.isEmpty()) {
                throw new IllegalStateException("Ingredients must be provided");
            }
            return new Pancake(ingredients);
        }
    }
}
