package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.User.Token;
import ar.edu.itba.paw.persistence.VerificationTokenDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class VerificationTokenServiceImpl implements VerificationTokenService{
    private final VerificationTokenDao verificationTokenDao;
    private static final int EXPIRATION = 60 * 24;

    @Autowired
    public VerificationTokenServiceImpl(VerificationTokenDao verificationTokenDao) {
        this.verificationTokenDao = verificationTokenDao;
    }

    @Transactional
    @Override
    public String createVerificationToken(int userId) {
        String token = UUID.randomUUID().toString();
        verificationTokenDao.createVerificationToken(userId,token,calculateExpirationDate());
        return token;
    }

    private Date calculateExpirationDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Timestamp(calendar.getTime().getTime()));
        calendar.add(Calendar.MINUTE, VerificationTokenServiceImpl.EXPIRATION);
        return new Date(calendar.getTime().getTime());
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Token> getToken(String token) {
        return verificationTokenDao.getToken(token);
    }

    @Transactional
    @Override
    public void deleteToken(Token token) {
        verificationTokenDao.deleteToken(token);
    }

    @Override
    public boolean isValidToken(Token token) {
        Calendar calendar = Calendar.getInstance();
        long aux1 = token.getExpirationDate().getTime() ;
        long aux2 = calendar.getTime().getTime();
        return aux1 > aux2;
    }

    @Transactional
    @Override
    public void renewToken(String token) {
        verificationTokenDao.renewToken(token,calculateExpirationDate());
    }
}
