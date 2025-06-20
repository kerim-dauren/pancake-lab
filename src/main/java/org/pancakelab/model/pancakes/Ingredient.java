package org.pancakelab.model.pancakes;

/**
 * Represents the ingredients used in pancakes at the Pancake Lab.
 * Each ingredient has a description that can be used for display purposes.
 */
public enum Ingredient {
    DARK_CHOCOLATE("dark chocolate"),
    WHIPPED_CREAM("whipped cream"),
    MILK_CHOCOLATE("milk chocolate"),
    HAZELNUTS("hazelnuts");

    private final String description;

    Ingredient(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
