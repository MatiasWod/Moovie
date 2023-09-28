package ar.edu.itba.paw.exceptions;

import org.springframework.security.core.AuthenticationException;

public class UserNotVerifiedException extends AuthenticationException {
    public UserNotVerifiedException(String msg, Throwable t) {
        super(msg, t);
    }

    public UserNotVerifiedException(String msg) {
        super(msg);
    }
}
