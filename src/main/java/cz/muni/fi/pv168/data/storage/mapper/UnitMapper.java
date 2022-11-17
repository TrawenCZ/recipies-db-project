package cz.muni.fi.pv168.data.storage.mapper;

import cz.muni.fi.pv168.data.storage.entity.UnitEntity;
import cz.muni.fi.pv168.data.storage.repository.UnitRepository;
import cz.muni.fi.pv168.data.validation.Validator;
import cz.muni.fi.pv168.model.BaseUnitsEnum;
import cz.muni.fi.pv168.model.Unit;

public class UnitMapper implements EntityMapper<UnitEntity, Unit> {

    private final Validator<Unit> unitValidator;
    private final UnitRepository units;

    public UnitMapper(Validator<Unit> unitValidator, UnitRepository units) {
        this.unitValidator = unitValidator;
        this.units = units;
    }
    @Override
    public UnitEntity mapToEntity(Unit source) {
        unitValidator.validate(source).intoException();

        Unit baseUnit = units.findByName(source.getBaseUnit().getValue()).orElseThrow();
        return new UnitEntity(source.getId(), source.getName(), source.getValueInBaseUnit(), baseUnit.getId());
    }

    @Override
    public Unit mapToModel(UnitEntity entity) {
        Unit unit = units.findById(entity.baseUnitId()).orElseThrow();
        BaseUnitsEnum baseUnit = switch (unit.getName()) {
            case "g" -> BaseUnitsEnum.GRAM;
            case "ml" -> BaseUnitsEnum.MILLILITER;
            case "pc(s)" -> BaseUnitsEnum.PIECE;
            default -> null;
        };

        return new Unit(entity.id(), entity.name(), entity.value(), baseUnit);
    }
}
