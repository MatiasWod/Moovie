package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.User.Token;

import java.util.Date;
import java.util.Optional;

public interface VerificationTokenDao {
    void createVerificationToken(int userId, String token, Date expirationDate);

    Optional<Token> getToken(String token);

    void deleteToken(Token token);

    void renewToken(String token, Date newExpirationDate);
}
