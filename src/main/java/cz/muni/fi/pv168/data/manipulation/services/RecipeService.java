package cz.muni.fi.pv168.data.manipulation.services;

import cz.muni.fi.pv168.data.storage.db.ConnectionHandler;
import cz.muni.fi.pv168.data.storage.db.TransactionHandler;
import cz.muni.fi.pv168.data.storage.repository.RecipeRepository;
import cz.muni.fi.pv168.data.storage.repository.Repository;
import cz.muni.fi.pv168.model.Recipe;

import java.util.function.Supplier;

/**
 * @author Radim Stejskal, Jan Martinek
 */
public class RecipeService extends ServiceImpl<Recipe> {

    public RecipeService(Repository<Recipe> recipeRepository, Supplier<TransactionHandler> transactions) {
        super(recipeRepository, transactions);
    }

    @Override
    protected void create(Recipe entity, Supplier<ConnectionHandler> connection) {
        repository.uncomitted(entity, ((RecipeRepository) repository)::createUncommited, connection);
    }

    @Override
    protected void update(Recipe entity, Supplier<ConnectionHandler> connection) {
        repository.uncomitted(entity, ((RecipeRepository) repository)::updateUncommited, connection);
    }
}
