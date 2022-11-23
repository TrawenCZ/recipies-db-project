package cz.muni.fi.pv168.data.manipulation.services;

import cz.muni.fi.pv168.data.storage.db.ConnectionHandler;
import cz.muni.fi.pv168.data.storage.db.TransactionHandler;
import cz.muni.fi.pv168.data.storage.repository.Repository;
import cz.muni.fi.pv168.model.*;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author Radim Stejskal, Jan Martinek
 */
public class RecipeService extends ServiceImpl<Recipe> {

    private Repository<Category> categories;
    private Repository<Ingredient> ingredients;
    private Repository<Unit> units;

    public RecipeService(Repository<Recipe> recipes,
                         Repository<Category> categories,
                         Repository<Ingredient> ingredients,
                         Repository<Unit> units,
                         Supplier<TransactionHandler> transactions
    ) {
        super(recipes, transactions);
        this.categories = Objects.requireNonNull(categories);
        this.ingredients = Objects.requireNonNull(ingredients);
        this.units = Objects.requireNonNull(units);
    }

    @Override
    public int[] saveRecords(Collection<Recipe> records) {
        var rIngredient = records.stream().map(Recipe::getIngredients).flatMap(List::stream).collect(Collectors.toSet());
        Collection<Category> categoryRecords = records.stream().map(Recipe::getCategory).collect(Collectors.toSet());
        Collection<Ingredient> ingredientRecords = rIngredient.stream().map(RecipeIngredient::getIngredient).collect(Collectors.toSet());
        Collection<Unit> baseUnitRecords = rIngredient.stream().map(RecipeIngredient::getIngredient).map(Ingredient::getUnit).collect(Collectors.toSet());
        Collection<Unit> unitRecords = rIngredient.stream().map(RecipeIngredient::getUnit).filter(e -> !baseUnitRecords.contains(e)).collect(Collectors.toSet());

        Counter counter = new Counter();

        try (var transaction = transactions.get()) {
            doImport(categoryRecords, counter, categories, transaction::connection);
            doImport(baseUnitRecords, counter, units, transaction::connection);
            doImport(unitRecords, counter, units, transaction::connection);
            ingrImport(ingredientRecords, counter, transaction::connection);
            recpImport(records, counter, transaction::connection);
            transaction.commit();
        }
        return (counter.doReplace)
            ? new int[]{counter.imported, -counter.actioned}
            : new int[]{counter.imported, counter.actioned};
    }

    private void ingrImport(Collection<Ingredient> records, Counter counter, Supplier<ConnectionHandler> connection) {
        records.forEach(e -> setId(e, connection));
        super.doImport(records, counter, ingredients, connection);
    }

    private void recpImport(Collection<Recipe> records, Counter counter, Supplier<ConnectionHandler> connection) {
        records.forEach(e -> setId(e, connection));
        super.doImport(records, counter, repository, connection);
    }

    private void setId(Unit newEntity, Supplier<ConnectionHandler> connection) {
        newEntity.setId(units.findUncommitted(newEntity.getName(), connection).get().getId());
    }

    private void setId(Category newEntity, Supplier<ConnectionHandler> connection) {
        newEntity.setId(categories.findUncommitted(newEntity.getName(), connection).get().getId());
    }

    private void setId(Ingredient newEntity, Supplier<ConnectionHandler> connection) {
        setId(newEntity.getUnit(), connection);
    }

    private void setId(Recipe newEntity, Supplier<ConnectionHandler> connection) {
        setId(newEntity.getCategory(), connection);
        newEntity.getIngredients().forEach(i -> setId(i.getUnit(), connection));
        newEntity.getIngredients().forEach(i -> setId(i.getIngredient(), connection));
    }
}
