package ar.edu.itba.paw.exceptions;

public class FailedToInsertToListException extends RuntimeException {
    public FailedToInsertToListException() {
        super();
    }

    public FailedToInsertToListException(String message) {
        super(message);
    }

    public FailedToInsertToListException(String message, Throwable cause) {
        super(message, cause);
    }

    public FailedToInsertToListException(Throwable cause) {
        super(cause);
    }

    protected FailedToInsertToListException(String message, Throwable cause,
                                            boolean enableSuppression,
                                            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}