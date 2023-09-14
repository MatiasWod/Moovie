package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class UserDaoJdbcImpl implements UserDao{
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert userJdbcInsert;

    private static final RowMapper<User> USER_ROW_MAPPER = (rs, rowNum) -> new User(
            rs.getInt("userId"),
            rs.getString("email")
    );


    @Autowired
    public UserDaoJdbcImpl(final DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
        userJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("users").usingGeneratedKeyColumns("userId");
        jdbcTemplate.execute(
                "CREATE TABLE IF NOT EXISTS users (" +
                        "userId SERIAL PRIMARY KEY," +
                        "email VARCHAR(255) UNIQUE NOT NULL," +
                        "CONSTRAINT valid_email_address CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$') )");
    }

    @Override
    public User createUser(String email) {
        jdbcTemplate.query("INSERT INTO users(email) VALUES( ? )", new Object[]{email}, USER_ROW_MAPPER);
        return jdbcTemplate.query("SELECT * FROM users WHERE email = ?", new Object[]{email}, USER_ROW_MAPPER).get(0);
    }

    @Override
    public User findUserById(int userId) {
        return jdbcTemplate.query("SELECT * FROM users WHERE userId = ?", new Object[]{userId}, USER_ROW_MAPPER).get(0);
    }

    @Override
    public User findUserByEmail(String email) {
        return jdbcTemplate.query("SELECT * FROM users WHERE email = ?", new Object[]{email}, USER_ROW_MAPPER).get(0);
    }


}
