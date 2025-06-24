package ar.edu.itba.paw.exceptions.authentication;

import org.springframework.security.access.AccessDeniedException;

public class UserBannedException extends AccessDeniedException {
    public UserBannedException(String message) {
        super(message);
    }
}
