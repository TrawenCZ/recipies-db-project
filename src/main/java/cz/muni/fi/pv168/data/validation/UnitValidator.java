package cz.muni.fi.pv168.data.validation;

import cz.muni.fi.pv168.model.Unit;

public class UnitValidator implements Validator<Unit> {

    @Override
    public ValidationResult validate(Unit unit) {
        var result = new ValidationResult();

        validateStringLength("Unit name", unit.getName(), 1, 100)
                .ifPresent(result::add);
        validateDoubleValue(unit.getValueInBaseUnit(), 0.001d, Double.MAX_VALUE - 1)
                .ifPresent(result::add);

        return result;
    }
}
