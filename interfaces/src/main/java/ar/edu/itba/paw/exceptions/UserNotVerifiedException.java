package ar.edu.itba.paw.exceptions;

public class UserNotVerifiedException extends RuntimeException{
    public UserNotVerifiedException() {
    }

    public UserNotVerifiedException(String message) {
        super(message);
    }

    public UserNotVerifiedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotVerifiedException(Throwable cause) {
        super(cause);
    }

    public UserNotVerifiedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
