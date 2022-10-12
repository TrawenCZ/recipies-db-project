package cz.muni.fi.pv168.data;

import cz.muni.fi.pv168.model.Ingredient;

import java.util.Collections;
import java.util.List;

public class IngredientDataGenerator extends AbstractDataGenerator<Ingredient> {

    private static final UnitDataGenerator unitGenerator = new UnitDataGenerator();

    private static final List<Ingredient> INGREDIENTS = List.of(
        new Ingredient("Mrkev", 1.35d, unitGenerator.createTestEntity()),
        new Ingredient("Rajče", 1.33d, unitGenerator.createTestEntity()),
        new Ingredient("Paprika", 1.47d, unitGenerator.createTestEntity()),
        new Ingredient("Kuřecí maso", 6.54d, unitGenerator.createTestEntity()),
        new Ingredient("Vepřové maso", 7.21d, unitGenerator.createTestEntity()),
        new Ingredient("Máslo", 4.74d, unitGenerator.createTestEntity()),
        new Ingredient("Mléko", 5d, unitGenerator.createTestEntity()),
        new Ingredient("Mouka", 2d, unitGenerator.createTestEntity()),
        new Ingredient("Voda", 1d, unitGenerator.createTestEntity()),
        new Ingredient("Cukr", 9.99d, unitGenerator.createTestEntity()),
        new Ingredient("Sůl", 0.11d, unitGenerator.createTestEntity()),
        new Ingredient("Pepř", 0.28d, unitGenerator.createTestEntity()),
        new Ingredient("Brambor", 8.34d, unitGenerator.createTestEntity()),
        new Ingredient("Sýr", 5.3d, unitGenerator.createTestEntity())
    );

    public Ingredient createTestEntity() {
        return selectRandom(INGREDIENTS);
    }

    public static List<Ingredient> getAll() {
        return Collections.unmodifiableList(INGREDIENTS);
    }

}
