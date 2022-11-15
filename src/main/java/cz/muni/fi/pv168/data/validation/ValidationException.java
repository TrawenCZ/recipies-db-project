package cz.muni.fi.pv168.data.validation;

import cz.muni.fi.pv168.data.storage.DataStorageException;

import java.util.Collections;
import java.util.List;

public class ValidationException extends DataStorageException {
    private final List<String> validationErrors;

    public ValidationException(String message, List<String> validationErrors) {
        super(message);
        this.validationErrors = validationErrors;
    }

    public List<String> getValidationErrors() {
        return Collections.unmodifiableList(validationErrors);
    }
}
