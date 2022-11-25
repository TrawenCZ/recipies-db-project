package cz.muni.fi.pv168.data.importers;

import cz.muni.fi.pv168.data.manipulation.importers.ObjectImporter;
import cz.muni.fi.pv168.data.storage.db.DatabaseManager;
import cz.muni.fi.pv168.model.BaseUnitsEnum;
import cz.muni.fi.pv168.model.Category;
import cz.muni.fi.pv168.model.Ingredient;
import cz.muni.fi.pv168.model.Nameable;
import cz.muni.fi.pv168.model.Recipe;
import cz.muni.fi.pv168.model.RecipeIngredient;
import cz.muni.fi.pv168.model.Unit;
import cz.muni.fi.pv168.wiring.TestDependencyProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RecipeImportTest {
    private DatabaseManager databaseManager;
    private ObjectImporter<Recipe> recipeImporter;

    @BeforeEach
    void setUp() {
        this.databaseManager = DatabaseManager.createTestInstance(true);
        var dependencyProvider = new TestDependencyProvider(databaseManager);
        this.recipeImporter = dependencyProvider.getRecipeImporter();
    }

    @AfterEach
    void tearDown() {
        databaseManager.destroySchema();
    }

    @Test
    void shouldSaveAllRecipes() {
//        List<Unit> units = List.of(new Unit("tons", 1000000, BaseUnitsEnum.GRAM),
//                new Unit("glass", 300, BaseUnitsEnum.GRAM));
//        List<Ingredient> Ingredients = List.of(new Ingredient("mleko", 10d, units.get(1)));
//
//
//        List<Recipe> recipes = List.of(
//                new Recipe("koprovka", "moc lidi ji nema rado", "pridej kopr",
//                        new Category("omacky", Color.orange), 50, 2,
//                        List.of(
//                                new RecipeIngredient(
//                                        new Ingredient("kopr", 400,
//                                                new Unit("tons", 1000000, BaseUnitsEnum.GRAM))))),
//                )
//        List<Unit> units = List.of(new Unit("ton", 1000000d, BaseUnitsEnum.GRAM),
//                new Unit("dolphin", 1, BaseUnitsEnum.PIECE),
//                new Unit("cup", 250d,BaseUnitsEnum.MILLILITER));
//        unitService.saveRecords(units);
//        List<String> saved = unitService.findAll().stream().map(Nameable::getName).toList();
//        List<String> toSave = units.stream().map(Nameable::getName).toList();
//        assertThat(saved).containsAll(toSave);
    }
}
