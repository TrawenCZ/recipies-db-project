package cz.muni.fi.pv168.data.storage.mapper;

import cz.muni.fi.pv168.data.storage.entity.IngredientEntity;
import cz.muni.fi.pv168.data.storage.repository.UnitRepository;
import cz.muni.fi.pv168.data.validation.Validator;
import cz.muni.fi.pv168.model.Ingredient;
import cz.muni.fi.pv168.model.Unit;

public class IngredientMapper implements EntityMapper<IngredientEntity, Ingredient> {

    private final Validator<Ingredient> ingredientValidator;
    private final UnitRepository units;

    public IngredientMapper(
            Validator<Ingredient> ingredientValidator,
            UnitRepository units
    ) {
        this.ingredientValidator = ingredientValidator;
        this.units = units;
    }

    @Override
    public IngredientEntity mapToEntity(Ingredient source) {
        ingredientValidator.validate(source).intoException();

        return new IngredientEntity(
                source.getId(),
                source.getName(),
                source.getKcal(),
                source.getUnit().getId());
    }

    @Override
    public Ingredient mapToModel(IngredientEntity entity) {
        Unit unit = units.findById(entity.baseUnitId()).orElseThrow();
        return new Ingredient(
                entity.id(),
                entity.name(),
                entity.kcalPerUnit(),
                unit);
    }
}
