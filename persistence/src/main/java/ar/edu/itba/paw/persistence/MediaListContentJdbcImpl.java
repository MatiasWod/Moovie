package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.MediaList.MediaList;
import ar.edu.itba.paw.models.MediaList.MediaListContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Optional;

@Repository
public class MediaListContentJdbcImpl implements MediaListContentDao{

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert mediaListContentJdbcInsert;

    private static final RowMapper<MediaListContent> MEDIA_LIST_CONTENT_ROW_MAPPER = (rs, rowNum) -> new MediaListContent(
            rs.getInt("mediaListId"),
            rs.getInt("mediaId"),
            rs.getString("status")
    );

    @Autowired
    public MediaListContentJdbcImpl(final DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
        mediaListContentJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("mediaListsContent").usingGeneratedKeyColumns("mediaListId");
        jdbcTemplate.execute(
                "CREATE TABLE IF NOT EXISTS mediaListsContent(" +
                        "mediaListId                        INTEGER NOT NULL," +
                        "mediaId                            INTEGER NOT NULL," +
                        "status                             VARCHAR(30)," +
                        "UNIQUE(mediaListId,mediaId)," +
                        "FOREIGN KEY(mediaListId) REFERENCES mediaLists(mediaListId) ON DELETE CASCADE," +
                        "FOREIGN KEY(mediaId) REFERENCES media(mediaId) ON DELETE CASCADE)");
    }

    @Override
    public Optional<MediaListContent> getMediaListContentById(int mediaListId) {
        return jdbcTemplate.query("SELECT * FROM mediaListsContent WHERE mediaListId = ?",new Object[]{mediaListId},MEDIA_LIST_CONTENT_ROW_MAPPER).stream().findFirst();
    }
}
