package cz.muni.fi.pv168.wiring;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import cz.muni.fi.pv168.data.manipulation.JsonExporter;
import cz.muni.fi.pv168.data.manipulation.JsonExporterImpl;
import cz.muni.fi.pv168.data.manipulation.JsonImporter;
import cz.muni.fi.pv168.data.manipulation.JsonImporterImpl;
import cz.muni.fi.pv168.data.service.*;
import cz.muni.fi.pv168.data.storage.dao.*;
import cz.muni.fi.pv168.data.storage.db.DatabaseManager;
import cz.muni.fi.pv168.data.storage.mapper.*;
import cz.muni.fi.pv168.data.storage.repository.*;
import cz.muni.fi.pv168.data.validation.*;
import cz.muni.fi.pv168.gui.action.ExportAction;
import cz.muni.fi.pv168.gui.action.ImportAction;
import cz.muni.fi.pv168.model.*;

/**
 * Dependency provider common for all environments
 *
 * @author Jan Martinek
 */
public abstract class CommonDependencyProvider implements DependencyProvider {

    private final DatabaseManager databaseManager;
    private final Repository<Recipe> recipes;
    private final Repository<Category> categories;
    private final Repository<Ingredient> ingredients;
    private final Repository<Unit> units;
    private final JsonImporter jsonImporter;
    private final JsonExporter jsonExporter;
    private final Map<String, ImportAction<?>> importActionMap;
    private Map<String, ExportAction<?>> exportActionMap;

    private final Service<Recipe> recipeService;
    private final Service<Category> categoryService;
    private final Service<Ingredient> ingredientService;
    private final Service<Unit> unitService;

    protected CommonDependencyProvider(DatabaseManager databaseManager) {
        this.databaseManager = Objects.requireNonNull(databaseManager);

        Validator<Recipe> recipeValidator = new RecipeValidator();
        Validator<Category> categoryValidator = new CategoryValidator();
        Validator<Ingredient> ingredientValidator = new IngredientValidator();
        Validator<Unit> unitValidator = new UnitValidator();
        jsonImporter = new JsonImporterImpl();
        jsonExporter = new JsonExporterImpl();

        // repositories
        categories = new CategoryRepository(
            new CategoryDao(databaseManager::getConnectionHandler),
            new CategoryMapper(categoryValidator)
        );

        units = new UnitRepository(
            new UnitDao(databaseManager::getConnectionHandler),
            new UnitMapper(unitValidator)
        );

        ingredients = new IngredientRepository(
            new IngredientDao(databaseManager::getConnectionHandler),
            new IngredientMapper(ingredientValidator, units::findById)
        );

        var recipeIngredientDao = new RecipeIngredientDao(databaseManager::getConnectionHandler);
        var recipeIngredientMapper = new RecipeIngredientMapper(units::findById, ingredients::findById);

        recipes = new RecipeRepository(
            new RecipeDao(databaseManager::getConnectionHandler),
            new RecipeMapper(recipeValidator, categories::findById, recipeIngredientDao::findByRecipeId, recipeIngredientMapper),
            recipeIngredientDao,
            recipeIngredientMapper
        );

        categoryService = new CategoryService(categories, databaseManager::getTransactionHandler);
        unitService = new UnitsService(units, databaseManager::getTransactionHandler);
        ingredientService = new IngredientService(ingredients, unitService, databaseManager::getTransactionHandler);
        recipeService = new RecipeService(recipes, unitService, categoryService, ingredientService, databaseManager::getTransactionHandler);
        importActionMap = initImportsMap();
        // exportActionMap = initExportMap();
    }

    private Map<String, ImportAction<?>> initImportsMap() {
        Map<String, ImportAction<?>> imports = new HashMap<>();
        imports.put(TabNamesEnum.RECIPES.getName(), new ImportAction<>(this.getJsonImporter(),
                (RecipeService) this.getRecipeService(),
                Recipe.class));
        imports.put(TabNamesEnum.CATEGORIES.getName(), new ImportAction<>(this.getJsonImporter(),
                (CategoryService) this.getCategoryService(), Category.class));
        imports.put(TabNamesEnum.UNITS.getName(), new ImportAction<>(this.getJsonImporter(),
                (UnitsService) this.getUnitService(), Unit.class));
        imports.put(TabNamesEnum.INGREDIENTS.getName(), new ImportAction<>(this.getJsonImporter(),
                (IngredientService) this.getIngredientService(), Ingredient.class));
        return imports;
    }

    //TODO: inject tables
    private Map<String, ExportAction<?>> initExportMap() {
        Map<String, ExportAction<?>> exports = new HashMap<>();
        exports.put(TabNamesEnum.RECIPES.getName(), new ExportAction<>(null, this.getJsonExporter(),
                (RecipeService) this.getRecipeService()));
        exports.put(TabNamesEnum.CATEGORIES.getName(), new ExportAction<>(null, this.getJsonExporter(),
                (CategoryService) this.getCategoryService()));
        exports.put(TabNamesEnum.UNITS.getName(), new ExportAction<>(null, this.getJsonExporter(),
                (UnitsService) this.getUnitService()));
        exports.put(TabNamesEnum.INGREDIENTS.getName(), new ExportAction<>(null, this.getJsonExporter(),
                (IngredientService) this.getIngredientService()));
        return exports;
    }

    @Override
    public ImportAction<?> getImportAction(String name) {
        return importActionMap.get(name);
    }

    @Override
    public ExportAction<?> getExportAction(String name) {
        return exportActionMap.get(name);
    }

    @Override
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    @Override
    public Repository<Recipe> getRecipeRepository() {
        return recipes;
    }

    @Override
    public Repository<Category> getCategoryRepository() {
        return categories;
    }

    @Override
    public Repository<Ingredient> getIngredientRepository() {
        return ingredients;
    }

    @Override
    public Repository<Unit> getUnitRepository() {
        return units;
    }

    @Override
    public JsonImporter getJsonImporter() {
        return jsonImporter;
    }

    @Override
    public JsonExporter getJsonExporter() {
        return jsonExporter;
    }

    @Override
    public Service<Recipe> getRecipeService() {
        return recipeService;
    }

    @Override
    public Service<Category> getCategoryService() {
        return categoryService;
    }

    @Override
    public Service<Ingredient> getIngredientService() {
        return ingredientService;
    }

    @Override
    public Service<Unit> getUnitService() {
        return unitService;
    }
}
