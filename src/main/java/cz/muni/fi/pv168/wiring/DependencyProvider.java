package cz.muni.fi.pv168.wiring;

import cz.muni.fi.pv168.data.manipulation.JsonExporter;
import cz.muni.fi.pv168.data.manipulation.JsonImporter;
import cz.muni.fi.pv168.gui.action.ExportAction;
import cz.muni.fi.pv168.gui.action.ImportAction;
import cz.muni.fi.pv168.model.*;
import cz.muni.fi.pv168.data.storage.db.DatabaseManager;
import cz.muni.fi.pv168.data.storage.repository.Repository;
import cz.muni.fi.pv168.data.service.Service;

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

    public JsonImporter getJsonImporter();

    public JsonExporter getJsonExporter();

    public ImportAction<?> getImportAction(String name);

    public ExportAction<?> getExportAction(String name);

}
