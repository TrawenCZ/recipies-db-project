package cz.muni.fi.pv168.data.service;

import cz.muni.fi.pv168.exceptions.InconsistentRecordException;
import cz.muni.fi.pv168.gui.models.RecipeTableModel;
import cz.muni.fi.pv168.model.IngredientAmount;
import cz.muni.fi.pv168.model.Recipe;

import java.util.List;

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
    public int saveRecords(List<Recipe> records) throws InconsistentRecordException {
        records = verifyRecords(records);

        var categories = records.stream().map(Recipe::getCategory).toList();
        var ingredients = records.stream()
                                 .map(Recipe::getIngredients)
                                 .flatMap(List::stream)
                                 .map(IngredientAmount::getIngredient)
                                 .toList();
        var units = records.stream()
                           .map(Recipe::getIngredients)
                           .flatMap(List::stream)
                           .map(IngredientAmount::getUnit)
                           .toList();

        // verification before saving
        categories = categoryService.verifyRecords(categories);
        units = unitsService.verifyRecords(units);
        ingredients = ingredientService.verifyRecords(ingredients);

        // saving only after all are verified, no verification needed now
        categoryService.saveRecords(categories, true);
        unitsService.saveRecords(units, true);
        ingredientService.saveRecords(ingredients, true);

        records.forEach(repository::addRow);
        return records.size();
    }

    @Override
    public void deleteRecords(List<Recipe> records) {

    }
}
