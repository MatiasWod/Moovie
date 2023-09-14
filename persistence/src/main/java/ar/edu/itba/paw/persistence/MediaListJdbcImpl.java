package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.MediaList.MediaList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Optional;

@Repository
public class MediaListJdbcImpl implements MediaListDao{
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert mediaListJdbcInsert;

    private static final RowMapper<MediaList> MEDIA_LIST_ROW_MAPPER = (rs, rowNum) -> new MediaList(
            rs.getInt("mediaListId"),
            rs.getInt("userId"),
            rs.getString("name"),
            rs.getString("description")
    );

    @Autowired
    public MediaListJdbcImpl(final DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
        mediaListJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("mediaLists").usingGeneratedKeyColumns("mediaListId");
        jdbcTemplate.execute(
                "CREATE TABLE IF NOT EXISTS mediaLists(" +
                        "mediaListId                        SERIAL PRIMARY KEY," +
                        "userId                             INTEGER NOT NULL," +
                        "name                               VARCHAR(255) NOT NULL," +
                        "description                        TEXT," +
                        "FOREIGN KEY(userId) REFERENCES users(userId) ON DELETE CASCADE," +
                        "UNIQUE(userId,name))");
    }

    @Override
    public Optional<MediaList> getMediaListById(int mediaListId) {
        return jdbcTemplate.query("SELECT * FROM mediaLists WHERE mediaListId = ?",new Object[]{mediaListId},MEDIA_LIST_ROW_MAPPER).stream().findFirst();
    }
}
