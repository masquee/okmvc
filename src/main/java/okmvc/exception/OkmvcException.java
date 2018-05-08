package okmvc.exception;

public class OkmvcException extends RuntimeException {

    public OkmvcException() {
        super();
    }

    public OkmvcException(String message) {
        super(message);
    }

    public OkmvcException(String message, Throwable cause) {
        super(message, cause);
    }

    public OkmvcException(Throwable cause) {
        super(cause);
    }

}
