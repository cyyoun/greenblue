package cyy.greenblue.exception;

public class SoldOutException extends RuntimeException {
    public SoldOutException(String message) {
        super(message);
    }
}
