package cz.muni.fi.pv168.wiring;

import cz.muni.fi.pv168.model.*;
import cz.muni.fi.pv168.data.manipulation.Importer;
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

    public Importer getRecipeImporter();

    public Importer getCategoryImporter();

    public Importer getIngredientImporter();

    public Importer getUnitImporter();
}
