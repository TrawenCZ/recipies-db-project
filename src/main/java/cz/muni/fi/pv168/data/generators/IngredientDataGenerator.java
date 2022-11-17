package cz.muni.fi.pv168.data.generators;

import cz.muni.fi.pv168.model.Ingredient;

import java.util.Collections;
import java.util.List;

public class IngredientDataGenerator extends AbstractDataGenerator<Ingredient> {

    private static final UnitDataGenerator unitGenerator = new UnitDataGenerator();

    private static final List<Ingredient> INGREDIENTS = List.of(
        new Ingredient(0L,"Mrkev", 1.35d, unitGenerator.createTestEntity()),
        new Ingredient(0L,"Rajče", 1.33d, unitGenerator.createTestEntity()),
        new Ingredient(0L,"Paprika", 1.47d, unitGenerator.createTestEntity()),
        new Ingredient(0L,"Kuřecí maso", 6.54d, unitGenerator.createTestEntity()),
        new Ingredient(0L,"Vepřové maso", 7.21d, unitGenerator.createTestEntity()),
        new Ingredient(0L,"Máslo", 4.74d, unitGenerator.createTestEntity()),
        new Ingredient(0L,"Mléko", 5d, unitGenerator.createTestEntity()),
        new Ingredient(0L,"Mouka", 20d, unitGenerator.createTestEntity()),
        new Ingredient(0L,"Voda", 1d, unitGenerator.createTestEntity()),
        new Ingredient(0L,"Cukr", 9.99d, unitGenerator.createTestEntity()),
        new Ingredient(0L,"Sůl", 0.11d, unitGenerator.createTestEntity()),
        new Ingredient(0L,"Pepř", 0.28d, unitGenerator.createTestEntity()),
        new Ingredient(0L,"Brambor", 8.34d, unitGenerator.createTestEntity()),
        new Ingredient(0L,"Sýr", 500.3d, unitGenerator.createTestEntity())
    );

    public Ingredient createTestEntity() {
        return selectRandom(INGREDIENTS);
    }

    public static List<Ingredient> getAll() {
        return Collections.unmodifiableList(INGREDIENTS);
    }

}
