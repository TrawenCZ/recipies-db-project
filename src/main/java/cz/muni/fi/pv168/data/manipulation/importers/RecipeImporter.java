package cz.muni.fi.pv168.data.manipulation.importers;

import cz.muni.fi.pv168.model.Recipe;
import cz.muni.fi.pv168.data.manipulation.Operation;
import cz.muni.fi.pv168.data.manipulation.acceptors.ObjectAcceptor;
import cz.muni.fi.pv168.data.manipulation.acceptors.RecipeAcceptor;
import cz.muni.fi.pv168.data.storage.db.ConnectionHandler;
import cz.muni.fi.pv168.data.storage.db.TransactionHandler;
import cz.muni.fi.pv168.data.storage.repository.RecipeRepository;
import cz.muni.fi.pv168.data.storage.repository.RepositoryFactory;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Importer into database, handles the import of a recipe objects.
 *
 * @author Jan Martinek, Radim Stejskal
 */
public class RecipeImporter extends ObjectImporter<Recipe> {

    private final RepositoryFactory factory;

    public RecipeImporter(RepositoryFactory factory, Supplier<TransactionHandler> transactions) {
        super(null, transactions);
        this.factory = factory;
    }

    @Override
    protected ObjectAcceptor<Recipe> getAcceptor(Supplier<ConnectionHandler> connection, Consumer<Operation> submit) {
        factory.setConnection(connection);

        var categories = factory.buildCategoryRepository();
        var units = factory.buildUnitRepository();
        var ingredients = factory.buildIngredientRepository(units);

        return new RecipeAcceptor(
            (RecipeRepository) factory.buildRecipeRepository(() -> null, categories, units, ingredients),
            submit,
            categories,
            units,
            ingredients
        );
    }
}
