package cz.muni.fi.pv168.data.storage.mapper;

import java.util.Objects;

import cz.muni.fi.pv168.data.storage.entity.IngredientEntity;
import cz.muni.fi.pv168.data.validation.Validator;
import cz.muni.fi.pv168.model.Ingredient;
import cz.muni.fi.pv168.model.Unit;

public class IngredientMapper implements EntityMapper<IngredientEntity, Ingredient> {

    private final Validator<Ingredient> ingredientValidator;
    private final Lookup<Unit> unitSupplier;

    public IngredientMapper(Validator<Ingredient> ingredientValidator,
                            Lookup<Unit> unitSupplier
    ) {
        this.ingredientValidator = Objects.requireNonNull(ingredientValidator);
        this.unitSupplier = Objects.requireNonNull(unitSupplier);
    }

    @Override
    public IngredientEntity mapToEntity(Ingredient source) {
        ingredientValidator.validate(source).intoException();

        return new IngredientEntity(
                source.getId(),
                source.getName(),
                source.getKcal(),
                (source.getUnit() != null || source.getUnit().getId() == null) ? source.getUnit().getId() : 0
        );
    }

    @Override
    public Ingredient mapToModel(IngredientEntity entity) {
        Unit unit = unitSupplier.get(entity.baseUnitId()).orElseThrow();

        return new Ingredient(
                entity.id(),
                entity.name(),
                entity.kcalPerUnit(),
                unit
        );
    }
}
