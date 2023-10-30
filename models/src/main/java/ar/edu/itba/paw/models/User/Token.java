package ar.edu.itba.paw.models.User;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "verificationTokens")
public class Token {

    //Ver cómo traer el userId
    @Id
    private int userId;

    @Column(length = 100, nullable = false)
    private String token;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date expirationDate;

    //Para hibernate
    public Token(){

    }

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
