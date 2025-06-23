package org.pancakelab.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pancakelab.model.pancakes.DefaultPancakeFactory;
import org.pancakelab.model.pancakes.Ingredient;
import org.pancakelab.model.pancakes.PancakeFactory;
import org.pancakelab.model.pancakes.PancakeRecipe;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PancakeTest {
    private PancakeFactory pancakeFactory;

    @BeforeEach
    public void setUp() {
        pancakeFactory = new DefaultPancakeFactory();
    }

    @Test
    public void GivenPancakeDoesNotExist_WhenCreatingPancakeWithOneIngredient_ThenPancakeCreated_Test() {
        PancakeRecipe createdPancake = pancakeFactory.createRecipe(List.of(Ingredient.DARK_CHOCOLATE));

        assertNotNull(createdPancake);
        assertEquals("Delicious pancake with dark chocolate!", createdPancake.description());
        assertEquals(List.of(Ingredient.DARK_CHOCOLATE), createdPancake.ingredients());
    }

    @Test
    public void GivenPancakeWithMultipleIngredients_WhenCreatingPancakeWithMultipleIngredient_ThenPancakeCreatedWithCorrectDescription_Test() {
        PancakeRecipe createdPancake = pancakeFactory.createRecipe(List.of(Ingredient.MILK_CHOCOLATE, Ingredient.HAZELNUTS));

        assertNotNull(createdPancake);
        assertEquals("Delicious pancake with milk chocolate, hazelnuts!", createdPancake.description());
        assertEquals(List.of(Ingredient.MILK_CHOCOLATE, Ingredient.HAZELNUTS), createdPancake.ingredients());
    }
}
