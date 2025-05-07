package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.User.Token;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Primary
@Repository
public class VerificationTokenHibernateDao implements VerificationTokenDao{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Token createVerificationToken(int userId, String token, LocalDateTime expirationDate) {
        final Token toCreateToken = new Token(userId,token,expirationDate);
        entityManager.persist(toCreateToken);
        return toCreateToken;
    }

    @Override
    public Token refreshVerificationToken(String oldToken, String newToken, LocalDateTime expirationDate) {
        final Token token = getToken(oldToken).orElseThrow(() -> new IllegalArgumentException("Token not found"));
        token.setExpirationDate(expirationDate);
        token.setToken(newToken);
        entityManager.merge(token);
        return token;
    }

    @Override
    public Optional<Token> getTokenByUserId(int userId) {
        final TypedQuery<Token> query = entityManager.createQuery("from Token where userId = :userId", Token.class);
        query.setParameter("userId",userId);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public Optional<Token> getToken(String token) {
        final TypedQuery<Token> query = entityManager.createQuery("from Token where token = :token", Token.class);
        query.setParameter("token",token);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public void deleteToken(Token token) {
        entityManager.remove(entityManager.contains(token) ? token : entityManager.merge(token));
    }
}
