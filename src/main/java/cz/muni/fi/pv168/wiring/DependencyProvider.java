package cz.muni.fi.pv168.wiring;

import cz.muni.fi.pv168.model.*;
import cz.muni.fi.pv168.data.manipulation.services.Service;
import cz.muni.fi.pv168.data.storage.db.DatabaseManager;
import cz.muni.fi.pv168.data.storage.repository.Repository;

/**
 * Dependency provider interface
 *
 * @author Jan Martinek
 */
public interface DependencyProvider {

    public DatabaseManager getDatabaseManager();

    public Repository<Recipe> getRecipeRepository();

    public Repository<Category> getCategoryRepository();

    public Repository<Ingredient> getIngredientRepository();

    public Repository<Unit> getUnitRepository();

    public Service<Recipe> getRecipeService();

    public Service<Category> getCategoryService();

    public Service<Ingredient> getIngredientService();

    public Service<Unit> getUnitService();

    public Service<?> getService(String name);
}
