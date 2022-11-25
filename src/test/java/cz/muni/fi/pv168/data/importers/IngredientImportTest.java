package cz.muni.fi.pv168.data.importers;

import cz.muni.fi.pv168.data.manipulation.importers.ObjectImporter;
import cz.muni.fi.pv168.data.storage.db.DatabaseManager;
import cz.muni.fi.pv168.data.storage.repository.Repository;
import cz.muni.fi.pv168.model.BaseUnitsEnum;
import cz.muni.fi.pv168.model.Category;
import cz.muni.fi.pv168.model.Ingredient;
import cz.muni.fi.pv168.model.Nameable;
import cz.muni.fi.pv168.model.Unit;
import cz.muni.fi.pv168.wiring.TestDependencyProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class IngredientImportTest {
    private DatabaseManager databaseManager;
    private ObjectImporter<Ingredient> ingredientImporter;
    private ObjectImporter<Unit> unitImporter;

    @BeforeEach
    void setUp() {
        this.databaseManager = DatabaseManager.createTestInstance(true);
        var dependencyProvider = new TestDependencyProvider(databaseManager);
        this.ingredientImporter = dependencyProvider.getIngredientImporter();
        this.unitImporter = dependencyProvider.getUnitImporter();
    }

    @AfterEach
    void tearDown() {
        databaseManager.destroySchema();
    }

    @Test
    void shouldSaveAllIngredientsAndTheirUnits() {
        List<Ingredient> ingredients = List.of(
                new Ingredient("carrot", 4d, new Unit("trolley", 80, BaseUnitsEnum.PIECE)),
                new Ingredient("milk", 4d, new Unit("glass", 300, BaseUnitsEnum.MILLILITER)),
                new Ingredient("potatoes", 4d, new Unit("trolley", 80, BaseUnitsEnum.PIECE)));
        ingredientImporter.saveRecords(ingredients);
        List<String> savedIngredients = ingredientImporter.findAll().stream().map(Nameable::getName).toList();
        List<String> ingredientsToSave = ingredients.stream().map(Nameable::getName).toList();
        List<String> unitsToSave = ingredients.stream().map(Ingredient::getUnit).map(Nameable::getName).toList();
        List<String> savedUnits = unitImporter.findAll().stream().map(Nameable::getName).toList();
        assertThat(savedIngredients).containsAll(ingredientsToSave);
        assertThat(savedUnits).containsAll(unitsToSave);
        System.out.println(unitImporter.findAll());
    }
}
