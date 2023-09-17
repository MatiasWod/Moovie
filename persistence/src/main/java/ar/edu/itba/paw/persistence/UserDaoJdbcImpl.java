package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.Review.Review;
import ar.edu.itba.paw.models.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
        userJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("users").usingGeneratedKeyColumns("userid");
    }

    @Override
    public User createUser(String email) {
        final Map<String, Object> args = new HashMap<>();
        args.put("email", email);

        final Number reviewId = userJdbcInsert.executeAndReturnKey(args);
        return new User(reviewId.intValue(), email);
    }

    @Override
    public Optional<User> findUserById(int userId) {
        return jdbcTemplate.query("SELECT * FROM users WHERE userId = ?", new Object[]{userId}, USER_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return jdbcTemplate.query("SELECT * FROM users WHERE email = ?", new Object[]{email}, USER_ROW_MAPPER).stream().findFirst();
    }


}
