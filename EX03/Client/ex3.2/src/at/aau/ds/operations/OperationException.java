package at.aau.ds.operations;

public class OperationException extends Exception {
    public OperationException(String message) {
        super(message);
    }

    public OperationException(String message, Throwable t) {
        super(message, t);
    }
}
