package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.models.MoovieList.MoovieListContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class MoovieListDaoJdbcImpl implements MoovieListDao{
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
    public MoovieListDaoJdbcImpl(final DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
        moovieListJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("moovieLists").usingGeneratedKeyColumns("moovielistid");
        moovieListContentJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("moovieListsContent");
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

    @Override
    public MoovieList createMoovieList(int userId, String name, String description) {
        final Map<String, Object> args = new HashMap<>();
        args.put("userId", userId);
        args.put("name", name);
        args.put("description", description);

        final Number moovieListId = moovieListJdbcInsert.executeAndReturnKey(args);
        return new MoovieList(moovieListId.intValue(), userId, name, description);
    }



    @Override
    public MoovieList createMoovieListWithContent(int userId, String name, String description, List<Integer> mediaIdList) {
        final MoovieList mL = createMoovieList(userId,name,description);

        return insertMediaIntoMoovieList(mL.getMoovieListId(), mediaIdList);
    }

    @Override
    public MoovieList insertMediaIntoMoovieList(int moovieListid, List<Integer> mediaIdList) {
        boolean repeatedElements = ( mediaIdList.stream().distinct().count() != mediaIdList.size() );
        if(repeatedElements){
            //Throw exception
        }

        final Map<String,Object> args = new HashMap<>();
        args.put("moovieListId", moovieListid);

        for(Integer mediaId : mediaIdList){
            args.put("mediaId", mediaId);
            moovieListContentJdbcInsert.execute(args);
        }

        return getMoovieListById(moovieListid).get();
    }
}


