package cz.muni.fi.pv168.exceptions;

public class UniqueException extends Exception{
    public UniqueException(){}
    public UniqueException(String message){
        super(message);
    }
    public UniqueException(String message, Throwable cause){
        super(message, cause);
    }

    public UniqueException(String tableName, String value) {
        super("Value '" + value +
                "' already exists in table '" + tableName +
                "'! ");
    }

    public UniqueException(String tableName, String value, int rowIndex) {
        super("Value '" + value +
                "' already exists in table '" + tableName +
                "' (found at row: " + rowIndex +
                ")! ");
    }
}
