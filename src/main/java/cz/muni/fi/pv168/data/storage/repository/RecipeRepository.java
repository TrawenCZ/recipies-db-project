package cz.muni.fi.pv168.data.storage.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
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
import cz.muni.fi.pv168.wiring.Supported;

/**
 * @author Jan Martinek
 */
public class RecipeRepository extends AbstractRepository<RecipeDao, RecipeEntity, Recipe> {

    private final Supplier<TransactionHandler> transactions;
    private final RecipeIngredientDao recipeIngredientDao;
    private final EntityMapper<RecipeIngredientEntity, RecipeIngredient> recipeIngredientMapper;

    public RecipeRepository(RecipeDao dao,
                            EntityMapper<RecipeEntity, Recipe> mapper,
                            RecipeIngredientDao recipeIngredientDao,
                            EntityMapper<RecipeIngredientEntity, RecipeIngredient> recipeIngredientMapper,
                            Supplier<TransactionHandler> transactions
    ) {
        super(dao, mapper, false);
        this.recipeIngredientDao = Objects.requireNonNull(recipeIngredientDao);
        this.recipeIngredientMapper = Objects.requireNonNull(recipeIngredientMapper);
        this.transactions = Objects.requireNonNull(transactions);
        refresh();
    }

    @Override
    public void create(Recipe entity) {
        try (var transaction = transactions.get()) {
            byConnection(entity, this::createUncommitted, transaction::connection);
            transaction.commit();
        }
    }

    public void createUncommitted(Recipe entity) {
        Stream.of(entity)
            .map(mapper::mapToEntity)
            .map(dao::create)
            .map(mapper::mapToModel)
            .map(e -> storeIngredients(entity.getIngredients(), e, recipeIngredientDao::create))
            .map(e -> fetchIngredients(e))
            .forEach(entities::add);
    }

    @Override
    public void update(Recipe entity) {
        try (var transaction = transactions.get()) {
            byConnection(entity, this::updateUncommitted, transaction::connection);
            transaction.commit();
        }
    }

    public void updateUncommitted(Recipe entity) {
        // delete removed ingredients
        recipeIngredientDao.findByRecipeId(entity.getId()).get().stream()
            .map(recipeIngredientMapper::mapToModel)
            .filter(e -> !containsIngredient(e.getId(), entity.getIngredients()))
            .forEach(e -> recipeIngredientDao.deleteById(e.getId()));

        var existing = recipeIngredientDao.findByRecipeId(entity.getId()).orElse(List.of()).stream()
                .map(recipeIngredientMapper::mapToModel)
                .toList();
        var toCreate = entity.getIngredients().stream()
            .filter(e -> !containsIngredient(e.getId(), existing))
            .toList();
        var toUpdate = entity.getIngredients().stream()
            .filter(e -> containsIngredient(e.getId(), existing))
            .toList();

        Stream.of(entity)
            .map(mapper::mapToEntity)
            .map(dao::update)
            .map(mapper::mapToModel)
            .map(e -> storeIngredients(toUpdate, e, recipeIngredientDao::update))
            .map(e -> storeIngredients(toCreate, e, recipeIngredientDao::create))
            .forEach(e -> entities.set(getIndex(e), fetchIngredients(e)));
    }

    @Override
    public String toString() {
        return Supported.RECIPE;
    }

    @Override
    protected void deleteEntityByIndex(int index) {
        try (var transaction = transactions.get()) {
            byConnection(entities.get(index), e -> dao.deleteById(e.getId()), transaction::connection);
            transaction.commit();
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
        recipe.setIngredients(recipeIngredientDao.findByRecipeId(recipe.getId()).orElseThrow().stream()
            .map(recipeIngredientMapper::mapToModel)
            .toList()
        );
        return recipe;
    }

    private boolean containsIngredient(Long id, List<RecipeIngredient> toCheck) {
        return toCheck.stream()
            .dropWhile(e -> id == null || id != e.getId())
            .findFirst()
            .isPresent();
    }

    private Recipe storeIngredients(List<RecipeIngredient> ingredients,
                                    Recipe recipe,
                                    Function<RecipeIngredientEntity, RecipeIngredientEntity> function
    ) {
        ingredients.stream()
            .map(e -> {e.setRecipeId(recipe.getId()); return e;})
            .map(recipeIngredientMapper::mapToEntity)
            .forEach(function::apply);
        return recipe;
    }

    private void byConnection(Recipe recipe, Consumer<Recipe> consumer, Supplier<ConnectionHandler> connection) {
        try {
            dao.customConnection(connection);
            recipeIngredientDao.customConnection(connection);
            consumer.accept(recipe);
        } finally {
            dao.defaultConnection();
            recipeIngredientDao.defaultConnection();
        }
    }
}
