package cz.muni.fi.pv168.data.service;

import cz.muni.fi.pv168.data.storage.db.TransactionHandler;
import cz.muni.fi.pv168.data.storage.repository.Repository;
import cz.muni.fi.pv168.exceptions.ImportVsDatabaseRecordsConflictException;
import cz.muni.fi.pv168.gui.Validator;
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
    )
    {
        super(recipeRepository, transactions);
        this.unitsService = unitsService;
        this.categoryService = categoryService;
        this.ingredientService = ingredientService;
    }

    @Override
    public int saveRecords(Collection<Recipe> records) throws ImportVsDatabaseRecordsConflictException {
        return saveRecords(records, false);
    }

    @Override
    public int saveRecords(Collection<Recipe> records, boolean skipInconsistent) throws ImportVsDatabaseRecordsConflictException {

        // verify recipes
        records = filterValidRecords(records, skipInconsistent);

        // verify all needed components
        var categories = categoryService.filterValidRecords(
                records.stream().map(Recipe::getCategory).toList(), skipInconsistent
        );
        var units = unitsService.filterValidRecords(
                records.stream()
                        .map(Recipe::getIngredients)
                        .flatMap(Collection::stream)
                        .map(RecipeIngredient::getUnit)
                        .toList(), skipInconsistent
        );
        var ingredients = ingredientService.filterValidRecords(
                records.stream()
                        .map(Recipe::getIngredients)
                        .flatMap(Collection::stream)
                        .map(RecipeIngredient::getIngredient)
                        .toList(), skipInconsistent
        );

        // save after successfull verification, which can be disabled now
        categoryService.saveRecords(categories, true);
        unitsService.saveRecords(units, true);
        ingredientService.saveRecords(ingredients, true);
        return saveRecords(records, true);
    }

    @Override
    public boolean validateRecordsBatch(Collection<Recipe> records) {
        var categories = records.stream().map(Recipe::getCategory).toList();
        var units = records.stream()
                        .map(Recipe::getIngredients)
                        .flatMap(Collection::stream)
                        .map(RecipeIngredient::getUnit)
                        .toList();
        var ingredients = records.stream()
                        .map(Recipe::getIngredients)
                        .flatMap(Collection::stream)
                        .map(RecipeIngredient::getIngredient)
                        .toList();

        return Validator.containsNonEqualNameDuplicates(categories)
                && Validator.containsNonEqualNameDuplicates(units)
                && Validator.containsNonEqualNameDuplicates(ingredients)
                && Validator.containsNonEqualNameDuplicates(records);
    }

    @Override
    public void deleteRecords(Collection<Recipe> records) {

    }
}
