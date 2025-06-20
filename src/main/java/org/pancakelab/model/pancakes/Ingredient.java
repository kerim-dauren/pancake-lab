package org.pancakelab.model.pancakes;

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
