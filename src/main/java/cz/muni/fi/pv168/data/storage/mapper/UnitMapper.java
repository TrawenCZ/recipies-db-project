package cz.muni.fi.pv168.data.storage.mapper;

import java.util.Objects;
import java.util.Optional;

import cz.muni.fi.pv168.data.storage.entity.UnitEntity;
import cz.muni.fi.pv168.data.validation.Validator;
import cz.muni.fi.pv168.model.BaseUnitsEnum;
import cz.muni.fi.pv168.model.Unit;

public class UnitMapper implements EntityMapper<UnitEntity, Unit> {

    @FunctionalInterface
    public interface NameLookup<T> {
        Optional<T> get(String name);
    }

    private final Validator<Unit> unitValidator;
    private Lookup<Unit> unitSupplier;
    private NameLookup<Unit> baseUnitSupplier;

    /**
     * Call {@link UnitMapper#updateSuppliers(Lookup, Lookup)} as soon
     * as possible (that is, after repository was created).
     *
     * <p> This needed to be done to keep repositories uniform.
     */
    public UnitMapper(Validator<Unit> unitValidator) {
        this.unitValidator = Objects.requireNonNull(unitValidator);
    }

    public void updateSuppliers(Lookup<Unit> unitSupplier, NameLookup<Unit> baseUnitSupplier) {
        this.unitSupplier = Objects.requireNonNull(unitSupplier);
        this.baseUnitSupplier = Objects.requireNonNull(baseUnitSupplier);
    }

    @Override
    public UnitEntity mapToEntity(Unit source) {
        unitValidator.validate(source).intoException();
        Unit baseUnit = baseUnitSupplier.get(source.getBaseUnit().getValue()).orElseThrow();

        return new UnitEntity(
            source.getId(),
            source.getName(),
            source.getValueInBaseUnit(),
            baseUnit.getId()
        );
    }

    @Override
    public Unit mapToModel(UnitEntity entity) {
        BaseUnitsEnum baseUnit = BaseUnitsEnum.valueOf(
            unitSupplier.get(entity.baseUnitId()).orElseThrow().getName()
        );

        return new Unit(
            entity.id(),
            entity.name(),
            entity.value(),
            baseUnit
        );
    }
}
