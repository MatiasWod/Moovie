package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.User.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class VerificationTokenDaoJdbcImpl implements VerificationTokenDao{
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    private static final RowMapper<Token> TOKEN_ROW_MAPPER = ((resultSet, i) -> new Token(
            resultSet.getInt("userId"),
            resultSet.getString("token"),
            resultSet.getDate("expirationDate")
    ));

    @Autowired
    public VerificationTokenDaoJdbcImpl(final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("verificationTokens");
    }

    @Override
    public void createVerificationToken(int userId, String token, Date expirationDate) {
        Map<String,Object> objectMap = new HashMap<>();
        objectMap.put("userId",userId);
        objectMap.put("token",token);
        objectMap.put("expirationDate",expirationDate);

        simpleJdbcInsert.execute(objectMap);
    }

    @Override
    public Optional<Token> getToken(String token) {
        return jdbcTemplate.query("SELECT * FROM verificationTokens WHERE token = ?", new Object[]{token},TOKEN_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public void deleteToken(Token token) {
        jdbcTemplate.update("DELETE FROM verificationTokens WHERE token = ?", token.getToken());
    }

    @Override
    public void renewToken(String token, Date newExpirationDate) {
        jdbcTemplate.update("UPDATE verificationTokens SET expirationDate = ? WHERE token = ?",newExpirationDate,token);
    }
}
