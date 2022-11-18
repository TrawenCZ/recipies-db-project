package cz.muni.fi.pv168.data.service;

import cz.muni.fi.pv168.data.storage.db.TransactionHandler;
import cz.muni.fi.pv168.data.storage.repository.Repository;
import cz.muni.fi.pv168.exceptions.InconsistentRecordException;
import cz.muni.fi.pv168.model.RecipeIngredient;
import cz.muni.fi.pv168.model.Unit;
import cz.muni.fi.pv168.model.Category;
import cz.muni.fi.pv168.model.Ingredient;
import cz.muni.fi.pv168.model.Recipe;

import java.util.Collection;
import java.util.function.Supplier;

/**
 * @author Radim Stejskal, Jan Martinek
 */
public class RecipeService extends AbstractService<Recipe> {

    private Service<Unit> unitsService;
    private Service<Category> categoryService;
    private Service<Ingredient> ingredientService;

    public RecipeService(Repository<Recipe> recipeRepository,
                         Service<Unit> unitsService,
                         Service<Category> categoryService,
                         Service<Ingredient> ingredientService,
                         Supplier<TransactionHandler> transactions
    ) {
        super(recipeRepository, transactions);
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
                   .map(RecipeIngredient::getUnit)
                   .toList()
        );
        var ingredients = ingredientService.verifyRecords(
            records.stream()
                   .map(Recipe::getIngredients)
                   .flatMap(Collection::stream)
                   .map(RecipeIngredient::getIngredient)
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
