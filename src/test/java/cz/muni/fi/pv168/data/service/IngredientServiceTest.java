package cz.muni.fi.pv168.data.service;

import cz.muni.fi.pv168.data.manipulation.services.Service;
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

public class IngredientServiceTest {
    private DatabaseManager databaseManager;
    private Service<Ingredient> ingredientService;
    private Service<Unit> unitService;

    @BeforeEach
    void setUp() {
        this.databaseManager = DatabaseManager.createTestInstance(true);
        var dependencyProvider = new TestDependencyProvider(databaseManager);
        this.ingredientService = dependencyProvider.getIngredientService();
        this.unitService = dependencyProvider.getUnitService();
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
        ingredientService.saveRecords(ingredients);
        List<String> savedIngredients = ingredientService.findAll().stream().map(Nameable::getName).toList();
        List<String> ingredientsToSave = ingredients.stream().map(Nameable::getName).toList();
        List<String> unitsToSave = ingredients.stream().map(Ingredient::getUnit).map(Nameable::getName).toList();
        List<String> savedUnits = unitService.findAll().stream().map(Nameable::getName).toList();
        assertThat(savedIngredients).containsAll(ingredientsToSave);
        assertThat(savedUnits).containsAll(unitsToSave);
        System.out.println(unitService.findAll());
    }
}
