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
}
