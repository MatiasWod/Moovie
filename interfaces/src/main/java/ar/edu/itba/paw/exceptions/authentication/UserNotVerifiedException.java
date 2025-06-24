package ar.edu.itba.paw.exceptions.authentication;

import org.springframework.security.access.AccessDeniedException;

public class UserNotVerifiedException extends AccessDeniedException {
    public UserNotVerifiedException(String message) {
        super(message);
    }
}
