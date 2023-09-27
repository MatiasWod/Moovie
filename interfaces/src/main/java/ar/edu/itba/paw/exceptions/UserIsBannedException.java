package ar.edu.itba.paw.exceptions;

public class UserIsBannedException extends RuntimeException{
    public UserIsBannedException() {
    }

    public UserIsBannedException(String message) {
        super(message);
    }

    public UserIsBannedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserIsBannedException(Throwable cause) {
        super(cause);
    }

    public UserIsBannedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
