package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.models.MoovieList.MoovieListContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class MoovieListJdbcImpl implements MoovieListDao{
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert moovieListJdbcInsert;
    private final SimpleJdbcInsert moovieListContentJdbcInsert;

    private static final RowMapper<MoovieList> MEDIA_LIST_ROW_MAPPER = (rs, rowNum) -> new MoovieList(
            rs.getInt("moovieListId"),
            rs.getInt("userId"),
            rs.getString("name"),
            rs.getString("description")
    );

    private static final RowMapper<MoovieListContent> MEDIA_LIST_CONTENT_ROW_MAPPER = (rs, rowNum) -> new MoovieListContent(
            rs.getInt("moovieListId"),
            rs.getInt("mediaId"),
            rs.getString("status")
    );

    @Autowired
    public MoovieListJdbcImpl(final DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
        moovieListJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("moovieLists").usingGeneratedKeyColumns("moovieListId");
        moovieListContentJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("moovieListsContent").usingGeneratedKeyColumns("moovieListId");
        jdbcTemplate.execute(
                "CREATE TABLE IF NOT EXISTS moovieLists(" +
                        "moovieListId                        SERIAL PRIMARY KEY," +
                        "userId                             INTEGER NOT NULL," +
                        "name                               VARCHAR(255) NOT NULL," +
                        "description                        TEXT," +
                        "FOREIGN KEY(userId) REFERENCES users(userId) ON DELETE CASCADE," +
                        "UNIQUE(userId,name))");

        jdbcTemplate.execute(
                "CREATE TABLE IF NOT EXISTS moovieListsContent(" +
                        "moovieListId                        INTEGER NOT NULL," +
                        "mediaId                            INTEGER NOT NULL," +
                        "status                             VARCHAR(30)," +
                        "UNIQUE(moovieListId,mediaId)," +
                        "FOREIGN KEY(moovieListId) REFERENCES moovieLists(moovieListId) ON DELETE CASCADE," +
                        "FOREIGN KEY(mediaId) REFERENCES media(mediaId) ON DELETE CASCADE)");
    }

    @Override
    public Optional<MoovieList> getMoovieListById(int moovieListId) {
        return jdbcTemplate.query("SELECT * FROM moovieLists WHERE moovieListId = ?",new Object[]{moovieListId},MEDIA_LIST_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public List<MoovieList> geAllMoovieLists() {
        return jdbcTemplate.query("SELECT * FROM moovieLists", MEDIA_LIST_ROW_MAPPER);
    }

    @Override
    public List<MoovieListContent> getMoovieListContentById(int moovieListId){
        return jdbcTemplate.query("SELECT * FROM moovieListsContent WHERE moovieListId = ? ORDER BY moovieListsContent.mediaId",new Object[]{moovieListId},MEDIA_LIST_CONTENT_ROW_MAPPER);
    }
}

