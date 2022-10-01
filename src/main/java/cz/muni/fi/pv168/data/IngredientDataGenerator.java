package cz.muni.fi.pv168.data;

import cz.muni.fi.pv168.model.Ingredient;

import java.util.Collections;
import java.util.List;

public class IngredientDataGenerator extends AbstractDataGenerator<Ingredient> {
    
    private static final List<Ingredient> INGREDIENTS = List.of(
        new Ingredient("Mrkev", 1.35d),
        new Ingredient("Rajče", 1.33d),
        new Ingredient("Paprika", 1.47d),
        new Ingredient("Kuřecí maso", 6.54d),
        new Ingredient("Vepřové maso", 7.21d),
        new Ingredient("Máslo", 4.74d),
        new Ingredient("Mléko", 5d),
        new Ingredient("Mouka", 2d),
        new Ingredient("Voda", 1d),
        new Ingredient("Cukr", 9.99d),
        new Ingredient("Sůl", 0.11d),
        new Ingredient("Pepř", 0.28d),
        new Ingredient("Brambor", 8.34d),
        new Ingredient("Sýr", 5.3d)
    );

    public Ingredient createTestEntity() {
        return selectRandom(INGREDIENTS);
    }

    public static List<Ingredient> getAll() {
        return Collections.unmodifiableList(INGREDIENTS);
    }

}
