package cz.muni.fi.pv168.data.generators;

import cz.muni.fi.pv168.model.Ingredient;

import java.util.Collections;
import java.util.List;

public class IngredientDataGenerator extends AbstractDataGenerator<Ingredient> {

    private static final UnitDataGenerator unitGenerator = new UnitDataGenerator();

    private static final List<Ingredient> INGREDIENTS = List.of(
        new Ingredient(0,"Mrkev", 1.35d, unitGenerator.createTestEntity()),
        new Ingredient(0,"Rajče", 1.33d, unitGenerator.createTestEntity()),
        new Ingredient(0,"Paprika", 1.47d, unitGenerator.createTestEntity()),
        new Ingredient(0,"Kuřecí maso", 6.54d, unitGenerator.createTestEntity()),
        new Ingredient(0,"Vepřové maso", 7.21d, unitGenerator.createTestEntity()),
        new Ingredient(0,"Máslo", 4.74d, unitGenerator.createTestEntity()),
        new Ingredient(0,"Mléko", 5d, unitGenerator.createTestEntity()),
        new Ingredient(0,"Mouka", 20d, unitGenerator.createTestEntity()),
        new Ingredient(0,"Voda", 1d, unitGenerator.createTestEntity()),
        new Ingredient(0,"Cukr", 9.99d, unitGenerator.createTestEntity()),
        new Ingredient(0,"Sůl", 0.11d, unitGenerator.createTestEntity()),
        new Ingredient(0,"Pepř", 0.28d, unitGenerator.createTestEntity()),
        new Ingredient(0,"Brambor", 8.34d, unitGenerator.createTestEntity()),
        new Ingredient(0,"Sýr", 500.3d, unitGenerator.createTestEntity())
    );

    public Ingredient createTestEntity() {
        return selectRandom(INGREDIENTS);
    }

    public static List<Ingredient> getAll() {
        return Collections.unmodifiableList(INGREDIENTS);
    }

}
