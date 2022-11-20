package cz.muni.fi.pv168.exceptions;

public class InconsistentSaveBatchException extends Exception {
    public InconsistentSaveBatchException() {
        super();
    }

    public InconsistentSaveBatchException(String message) {
        super(message);
    }
}
