package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.User.Token;
import ar.edu.itba.paw.models.User.User;

import java.util.Optional;

public interface VerificationTokenService {
    String createVerificationToken(int userId);

    Token manageUserToken(User user);

    Optional<Token> getToken(String token);

    Optional<Token> getTokenByUserId(int userId);

    void deleteToken(Token token);

    boolean isValidToken(Token token);

    void renewToken(Token token);
}
