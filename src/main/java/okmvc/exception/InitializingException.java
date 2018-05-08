package okmvc.exception;

public class InitializingException extends OkmvcException {

    public InitializingException() {
        super();
    }

    public InitializingException(String message) {
        super(message);
    }

    public InitializingException(String message, Throwable cause) {
        super(message, cause);
    }

    public InitializingException(Throwable cause) {
        super(cause);
    }

}
