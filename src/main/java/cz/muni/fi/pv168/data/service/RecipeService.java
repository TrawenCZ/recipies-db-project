package cz.muni.fi.pv168.data.service;

import cz.muni.fi.pv168.exceptions.InconsistentRecordException;
import cz.muni.fi.pv168.gui.models.RecipeTableModel;
import cz.muni.fi.pv168.model.IngredientAmount;
import cz.muni.fi.pv168.model.Recipe;

import java.util.Collection;

/**
 * @author Radim Stejskal, Jan Martinek
 */
public class RecipeService extends AbstractService<Recipe> {

    private UnitsService unitsService;
    private CategoryService categoryService;
    private IngredientService ingredientService;

    public RecipeService(RecipeTableModel recipeRepository,
                             UnitsService unitsService,
                             CategoryService categoryService,
                             IngredientService ingredientService) {
        super(recipeRepository, "Recipe");
        this.unitsService = unitsService;
        this.categoryService = categoryService;
        this.ingredientService = ingredientService;
    }

    @Override
    public int saveRecords(Collection<Recipe> records) throws InconsistentRecordException {

        // verify recipes
        records = verifyRecords(records);

        // verify all needed components
        var categories = categoryService.verifyRecords(
            records.stream().map(Recipe::getCategory).toList()
        );
        var units = unitsService.verifyRecords(
            records.stream()
                   .map(Recipe::getIngredients)
                   .flatMap(Collection::stream)
                   .map(IngredientAmount::getUnit)
                   .toList()
        );
        var ingredients = ingredientService.verifyRecords(
            records.stream()
                   .map(Recipe::getIngredients)
                   .flatMap(Collection::stream)
                   .map(IngredientAmount::getIngredient)
                   .toList()
        );

        // save after successfull verification, which can be disabled now
        categoryService.saveRecords(categories, true);
        unitsService.saveRecords(units, true);
        ingredientService.saveRecords(ingredients, true);
        return saveRecords(records, true);
    }

    @Override
    public void deleteRecords(Collection<Recipe> records) {

    }
}
