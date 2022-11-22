package cz.muni.fi.pv168.data.validation;

import java.util.List;
import java.util.Optional;

/**
 * Validator interface for validating the model
 *
 * @param <M> Model type
 */
@FunctionalInterface
public interface Validator<M> {

    ValidationResult validate(M model);

    default Validator<M> and(Validator<M> other) {
        return compose(List.of(this, other));
    }

    static <M> Validator<M> compose(List<Validator<M>> validators) {
        return model -> validators.stream()
                                   .map(x -> x.validate(model))
                                   .reduce(new ValidationResult(), (r, e) -> {
                                       r.add(e.getValidationErrors());
                                       return r;
                                   });
    }

    default Optional<String> validateStringLength(String name, String value, int min, int max) {
        if (value.length() < min) {
            return Optional.of(name + " is too short");
        } else if (value.length() > max) {
            return Optional.of(name + " is too long");
        }
        return Optional.empty();
    }

    default Optional<String> validateIntValue(int value, int min, int max) {
        if (value < min) return Optional.of(String.format("Value: %d is must be at least (%d)", value, min));
        if (value > max) return Optional.of(String.format("Value: %d is exceeds (%d)", value, max));
        return Optional.empty();
    }

    default Optional<String> validateDoubleValue(double value, double min, double max) {
        if (value < min) return Optional.of(String.format("Value: %.2f is must be at least (%.2f)", value, min));
        if (value > max) return Optional.of(String.format("Value: %.2f is exceeds (%.2f)", value, max));
        return Optional.empty();
    }
}
