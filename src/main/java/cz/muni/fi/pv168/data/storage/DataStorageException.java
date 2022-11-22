package cz.muni.fi.pv168.data.storage;

/**
 * Exception that is thrown if there is some problem with data storage
 */
public class DataStorageException extends RuntimeException {

    private static final long serialVersionUID = 0L;

    public DataStorageException(String message) {
        this(message, null);
    }

    public DataStorageException(String message, Throwable cause) {
        super("Storage error: " +  message, cause);
    }
}

