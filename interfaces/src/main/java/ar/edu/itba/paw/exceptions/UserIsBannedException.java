package ar.edu.itba.paw.exceptions;

import org.springframework.security.core.AuthenticationException;

public class UserIsBannedException extends AuthenticationException {
    public UserIsBannedException(String msg, Throwable t) {
        super(msg, t);
    }

    public UserIsBannedException(String msg) {
        super(msg);
    }
}
