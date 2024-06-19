package at.spengergasse.vaadin.exception;

public class MotoException extends RuntimeException {

    public MotoException(String message) {
        super(message);
    }

    public MotoException(String message, Throwable cause) {
        super(message, cause);
    }

    public MotoException(Throwable cause) {
        super(cause);
    }

    public MotoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
