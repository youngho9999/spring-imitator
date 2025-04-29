package live.exception;

public class ContextInitializeException extends RuntimeException {
    public ContextInitializeException(String message) {
        super(message);
    }

    public ContextInitializeException() {
    }

    public ContextInitializeException(Throwable cause) {
        super(cause);
    }
}
