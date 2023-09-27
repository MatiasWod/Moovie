package ar.edu.itba.paw.models.User;

import java.util.Date;

public class Token {
    private final int userId;
    private final String token;
    private final Date expirationDate;

    public Token(int userId, String token, Date expirationDate) {
        this.userId = userId;
        this.token = token;
        this.expirationDate = expirationDate;
    }

    public int getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }
}
