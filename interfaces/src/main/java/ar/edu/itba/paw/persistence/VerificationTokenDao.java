package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.User.Token;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

public interface VerificationTokenDao {
    Token createVerificationToken(int userId, String token, LocalDateTime expirationDate);

    Token refreshVerificationToken(String oldToken, String newToken, LocalDateTime expirationDate);

    Optional<Token> getTokenByUserId(int userId);

    Optional<Token> getToken(String token);

    void deleteToken(Token token);

}
