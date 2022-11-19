package cz.muni.fi.pv168.data.storage.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cz.muni.fi.pv168.data.storage.dao.RecipeDao;
import cz.muni.fi.pv168.data.storage.dao.RecipeIngredientDao;
import cz.muni.fi.pv168.data.storage.db.ConnectionHandler;
import cz.muni.fi.pv168.data.storage.db.TransactionHandler;
import cz.muni.fi.pv168.data.storage.entity.RecipeEntity;
import cz.muni.fi.pv168.data.storage.entity.RecipeIngredientEntity;
import cz.muni.fi.pv168.data.storage.mapper.EntityMapper;
import cz.muni.fi.pv168.model.Recipe;
import cz.muni.fi.pv168.model.RecipeIngredient;

/**
 * @author Jan Martinek
 */
public class RecipeRepository extends AbstractRepository<RecipeDao, RecipeEntity, Recipe> {

    private final Supplier<ConnectionHandler> connections;
    private final Supplier<TransactionHandler> transactions;
    private final RecipeIngredientDao ingredientDao;
    private final EntityMapper<RecipeIngredientEntity, RecipeIngredient> ingredientMapper;

    public RecipeRepository(RecipeDao dao,
                            EntityMapper<RecipeEntity, Recipe> mapper,
                            RecipeIngredientDao ingredientDao,
                            EntityMapper<RecipeIngredientEntity, RecipeIngredient> ingredientMapper,
                            Supplier<ConnectionHandler> connections,
                            Supplier<TransactionHandler> transactions
    ) {
        super(dao, mapper, false);
        this.ingredientDao = Objects.requireNonNull(ingredientDao);
        this.ingredientMapper = Objects.requireNonNull(ingredientMapper);
        this.connections = Objects.requireNonNull(connections);
        this.transactions = Objects.requireNonNull(transactions);
        refresh();
    }

    @Override
    public void create(Recipe newEntity) {
        try (var transaction = transactions.get()) {
            dao.changeConnection(transaction::connection);
            ingredientDao.changeConnection(transaction::connection);

            Stream.of(newEntity)
                  .map(mapper::mapToEntity)
                  .map(dao::create)
                  .map(mapper::mapToModel)
                  .map(e -> storeIngredients(newEntity.getIngredients(), e, ingredientDao::create))
                  .map(e -> fetchIngredients(e))
                  .forEach(entities::add);

            transaction.commit();
        } finally {
            dao.changeConnection(connections);
            ingredientDao.changeConnection(connections);
        }
    }

    @Override
    public void update(Recipe entity) {
        try (var transaction = transactions.get()) {
            dao.changeConnection(transaction::connection);
            ingredientDao.changeConnection(transaction::connection);

            List<RecipeIngredient> existing = ingredientDao.findByRecipeId(entity.getId()).get().stream().map(ingredientMapper::mapToModel).toList();
            var toCreate = entity.getIngredients().stream().filter(e -> e.getId() == null || !containsIngredient(e.getId(), existing)).toList();
            var toUpdate = existing.stream().filter(e -> containsIngredient(e.getId(), entity.getIngredients())).toList();

            // delete removed ingredients
            existing.stream()
                    .filter(e -> !containsIngredient(e.getId(), existing))
                    .forEach(e -> ingredientDao.deleteById(e.getId()));

            Stream.of(entity)
                  .map(mapper::mapToEntity)
                  .map(dao::update)
                  .map(mapper::mapToModel)
                  .map(e -> storeIngredients(toUpdate, e, ingredientDao::update))
                  .map(e -> storeIngredients(toCreate, e, ingredientDao::create));

            transaction.commit();
        } finally {
            dao.changeConnection(connections);
            ingredientDao.changeConnection(connections);
        }
    }

    @Override
    public void deleteEntityByIndex(int index) {
        var id = entities.get(index).getId();

        try (var transaction = transactions.get()) {
            dao.changeConnection(transaction::connection);
            ingredientDao.changeConnection(transaction::connection);

            ingredientDao.findAll().forEach(e -> ingredientDao.deleteById(e.id()));
            dao.deleteById(id);

            transaction.commit();
        } finally {
            dao.changeConnection(connections);
            ingredientDao.changeConnection(connections);
        }
    }

    @Override
    protected List<Recipe> fetchAllEntities() {
        return dao.findAll().stream()
            .map(mapper::mapToModel)
            .map(this::fetchIngredients)
            .collect(Collectors.toCollection(ArrayList::new));
    }

    private Recipe fetchIngredients(Recipe recipe) {
        recipe.setIngredients(ingredientDao.findByRecipeId(recipe.getId()).orElseThrow().stream()
            .map(ingredientMapper::mapToModel)
            .toList()
        );
        return recipe;
    }

    private boolean containsIngredient(long id, List<RecipeIngredient> toCheck) {
        return toCheck.stream()
            .dropWhile(e -> e.getId() == null || e.getId() != id)
            .findFirst()
            .isPresent();
    }

    private Recipe storeIngredients(List<RecipeIngredient> ingredients,
                                    Recipe recipe,
                                    Function<RecipeIngredientEntity, RecipeIngredientEntity> function
    ) {
        ingredients.stream()
            .map(e -> {e.setRecipeId(recipe.getId()); return e;})
            .map(ingredientMapper::mapToEntity)
            .forEach(function::apply);
        return recipe;
    }

    @Override
    public String toString() {
        return "Recipe";
    }
}
