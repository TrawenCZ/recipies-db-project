package cz.muni.fi.pv168.data.storage.mapper;

import java.util.Objects;
import java.util.Optional;

import cz.muni.fi.pv168.data.storage.entity.UnitEntity;
import cz.muni.fi.pv168.data.validation.Validator;
import cz.muni.fi.pv168.model.BaseUnitsEnum;
import cz.muni.fi.pv168.model.Unit;

/**
 * @author Jan Martinek
 */
public class UnitMapper implements EntityMapper<UnitEntity, Unit> {

    @FunctionalInterface
    public interface NameLookup<T> {
        Optional<T> get(String name);
    }

    private final Validator<Unit> unitValidator;
    private final Lookup<UnitEntity> unitSupplier;
    private NameLookup<Unit> baseUnitSupplier;

    /**
     * Call {@link UnitMapper#updateSuppliers(Lookup, Lookup)} as soon
     * as possible (that is, after repository was created).
     *
     * <p> This needed to be done to keep repositories uniform.
     */
    public UnitMapper(Validator<Unit> unitValidator, Lookup<UnitEntity> unitSupplier) {
        this.unitValidator = Objects.requireNonNull(unitValidator);
        this.unitSupplier = Objects.requireNonNull(unitSupplier);
    }

    public void setBaseUnitSupplier(NameLookup<Unit> baseUnitSupplier) {
        this.baseUnitSupplier = Objects.requireNonNull(baseUnitSupplier);
    }

    @Override
    public UnitEntity mapToEntity(Unit source) {
        unitValidator.validate(source).intoException();

        Optional<Unit> baseUnit = Optional.empty();
        if (source.getBaseUnit() != null) baseUnit = baseUnitSupplier.get(
            source.getBaseUnit().getValue()
        );

        return new UnitEntity(
            source.getId(),
            source.getName(),
            source.getValueInBaseUnit(),
            (baseUnit.isPresent() && source.getId() != baseUnit.get().getId())
                ? baseUnit.get().getId()
                : 0
        );
    }

    @Override
    public Unit mapToModel(UnitEntity entity) {
        Optional<UnitEntity> baseUnit = Optional.empty();
        if (entity.baseUnitId() != 0) baseUnit = unitSupplier.get(entity.baseUnitId());

        return new Unit(
            entity.id(),
            entity.name(),
            entity.amount(),
            (baseUnit.isPresent())
                ? BaseUnitsEnum.stringToEnum(baseUnit.get().name())
                : null
        );
    }
}
