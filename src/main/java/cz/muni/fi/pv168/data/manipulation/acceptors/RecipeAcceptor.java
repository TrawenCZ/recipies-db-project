package cz.muni.fi.pv168.data.manipulation.acceptors;

import java.util.function.Consumer;

import cz.muni.fi.pv168.data.manipulation.Operation;
import cz.muni.fi.pv168.data.storage.repository.RecipeRepository;
import cz.muni.fi.pv168.data.storage.repository.Repository;
import cz.muni.fi.pv168.model.Category;
import cz.muni.fi.pv168.model.Ingredient;
import cz.muni.fi.pv168.model.Recipe;
import cz.muni.fi.pv168.model.Unit;

/**
 * One time importer that is setup for a single transaction.
 * Transaction may have multiple importer instances, but only
 * one should be run at a time.
 *
 * @author Jan Martinek
 */
public class RecipeAcceptor extends ObjectAcceptor<Recipe> {

    private final ObjectAcceptor<Category> categorytAcceptor;
    private final ObjectAcceptor<Unit> unitAcceptor;
    private final ObjectAcceptor<Ingredient> ingredientAcceptor;

    public RecipeAcceptor(
        RecipeRepository repository,
        Consumer<Operation> publish,
        Repository<Category> categories,
        Repository<Unit> units,
        Repository<Ingredient> ingredients
    ) {
        super(repository::findByName, repository::createUncommitted, repository::updateUncommitted, publish);
        this.categorytAcceptor = new CategoryAcceptor(categories, publish);
        this.unitAcceptor = new UnitAcceptor(units, publish);
        this.ingredientAcceptor = new IngredientAcceptor(ingredients, publish, units);
    }

    @Override
    public void accept(Recipe item) {
        categorytAcceptor.accept(item.getCategory());
        item.getIngredients().forEach(e -> {
            unitAcceptor.accept(e.getUnit());
            ingredientAcceptor.accept(e.getIngredient());
        });
        super.accept(item);
    }

    @Override
    public void setReplace() {
        super.setReplace();
        categorytAcceptor.setReplace();
        unitAcceptor.setReplace();
        ingredientAcceptor.setReplace();
    }
}
