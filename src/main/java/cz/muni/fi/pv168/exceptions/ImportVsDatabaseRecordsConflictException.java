package cz.muni.fi.pv168.exceptions;

public class ImportVsDatabaseRecordsConflictException extends RuntimeException {
    public ImportVsDatabaseRecordsConflictException() {
        super();
    }

    public ImportVsDatabaseRecordsConflictException(String message) {
        super(message);
    }
}
