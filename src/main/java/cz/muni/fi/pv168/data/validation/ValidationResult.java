package cz.muni.fi.pv168.data.validation;

import java.util.*;

/**
 * Validation result stores the list of validation errors
 * if the list of errors is empty, the validation passed
 * otherwise the result is not valid
 */
public final class ValidationResult {

    private final List<String> validationErrors;

    public static ValidationResult failed(String... validationErrors) {
        return new ValidationResult(Arrays.asList(validationErrors));
    }
    public static ValidationResult success() {
        return new ValidationResult();
    }

    public ValidationResult(Collection<String> validationErrors) {
        this.validationErrors = new ArrayList<>(validationErrors);
    }

    public ValidationResult() {
        this(List.of());
    }

    public void add(String message) {
        if (message != null && !message.isEmpty()) {
            validationErrors.add(message);
        }
    }

    public void add(Collection<String> messages) {
        messages.forEach(this::add);
    }

    public List<String> getValidationErrors() {
        return Collections.unmodifiableList(validationErrors);
    }

    public boolean isValid() {
        return validationErrors.isEmpty();
    }

    @Override
    public String toString() {
        if (isValid()) {
            return "Validation passed";
        }
        return "Validation has failed:\n" + String.join("\n", getValidationErrors());
    }

    public void intoException() {
        if (!isValid()) {
            throw new ValidationException(toString(), validationErrors);
        }
    }
}
