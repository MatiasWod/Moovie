/*package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.exceptions.UnableToInsertIntoDatabase;
import ar.edu.itba.paw.models.BannedMessage.BannedMessage;
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
public class BannedDaoJdbcImpl implements BannedDao{
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert bannedMessageJdbcInsert;



    private static final RowMapper<BannedMessage> BANNED_MESSAGE_ROW_MAPPER = (rs, rowNum) -> new BannedMessage(
            rs.getInt("modUserId"),
            rs.getString("username"), //mod username
            rs.getString("message")

    );

    @Autowired
    public BannedDaoJdbcImpl(final DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
        bannedMessageJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("bannedMessage");
    }

    @Override
    public Optional<BannedMessage> getBannedMessage(int userId) {
        return jdbcTemplate.query("SELECT * from bannedMessage LEFT JOIN users ON users.userid = bannedMessage.moduserid WHERE bannedUserid = ? ;", new Object[]{userId}, BANNED_MESSAGE_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public void createBannedMessage(int bannedUserId, int modUserId, String message) {
        final Map<String,Object> args = new HashMap<>();
        args.put("moduserid", modUserId);
        args.put("bannedUserId", bannedUserId);
        args.put("message", message);
        try{
            bannedMessageJdbcInsert.execute(args);
        } catch (Exception e){
            throw new UnableToInsertIntoDatabase("BannedMessage creation failed, user already has a banned message");
        }
    }

    @Override
    public void deleteBannedMessage(int bannedUserId) {
        String sql = "DELETE FROM bannedMessage WHERE bannedUserid=?";
        jdbcTemplate.update( sql , new Object[]{bannedUserId} );
    }
}
*/