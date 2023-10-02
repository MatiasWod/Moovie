package ar.edu.itba.paw.exceptions;

public class InvalidMoovieListIdExcepetion extends RuntimeException{
    public InvalidMoovieListIdExcepetion() {
    }

    public InvalidMoovieListIdExcepetion(String message) {
        super(message);
    }

    public InvalidMoovieListIdExcepetion(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidMoovieListIdExcepetion(Throwable cause) {
        super(cause);
    }

    public InvalidMoovieListIdExcepetion(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
