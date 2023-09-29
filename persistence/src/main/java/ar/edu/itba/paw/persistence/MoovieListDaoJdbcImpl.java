package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.models.MoovieList.MoovieListContent;
import ar.edu.itba.paw.models.MoovieList.MoovieListLikes;
import ar.edu.itba.paw.models.User.User;
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
    private final SimpleJdbcInsert moovieListLikesJdbcInsert;
    private static final int INITIAL_LIKE_COUNT = 0;

    private static final RowMapper<MoovieList> MOOVIE_LIST_ROW_MAPPER = (rs, rowNum) -> new MoovieList(
            rs.getInt("moovieListId"),
            rs.getInt("userId"),
            rs.getString("name"),
            rs.getString("description"),
            rs.getInt("type")
    );

    private static final RowMapper<MoovieListContent> MOOVIE_LIST_CONTENT_ROW_MAPPER = (rs, rowNum) -> new MoovieListContent(
            rs.getInt("moovielistid"),
            rs.getInt("mediaId"),
            rs.getString("status")
    );

    private static final RowMapper<MoovieListLikes> MOOVIE_LIST_LIKES_ROW_MAPPER = (rs, rowNum) -> new MoovieListLikes(
            rs.getInt("moovielistid"),
            rs.getInt("userId")
    );

    private static final RowMapper<Integer> COUNT_ROW_MAPPER = ((resultSet, i) -> resultSet.getInt("count"));

    @Autowired
    public MoovieListDaoJdbcImpl(final DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
        moovieListJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("moovieLists").usingGeneratedKeyColumns("moovielistid");
        moovieListContentJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("moovieListsContent");
        moovieListLikesJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("moovieListsLikes");
    }

    /***
     * GENERAL QUERYS FORM MOOVIE LISTS
     */

    @Override
    public Optional<MoovieList> getMoovieListById(int moovieListId) {
        return jdbcTemplate.query("SELECT * FROM moovieLists WHERE moovieListId = ?",new Object[]{moovieListId},MOOVIE_LIST_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public List<MoovieList> getAllMoovieLists(int size, int pageNumber) {
        return jdbcTemplate.query("SELECT * FROM moovieLists WHERE type = ?  LIMIT ? OFFSET ?",new Object[]{MOOVIE_LIST_TYPE_STANDARD_PUBLIC, size, pageNumber*size} , MOOVIE_LIST_ROW_MAPPER);
    }

    @Override
    public List<MoovieListContent> getMoovieListContentById(int moovieListId){
        return jdbcTemplate.query("SELECT * FROM moovieListsContent WHERE moovieListId = ? ORDER BY moovieListsContent.mediaId",new Object[]{moovieListId},MOOVIE_LIST_CONTENT_ROW_MAPPER);
    }

    @Override
    public Optional<Integer> getMoovieListCount() {
        return jdbcTemplate.query("SELECT COUNT(*) AS count FROM moovieListsContent", COUNT_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public Optional<Integer> getMoovieListSize(int moovieListId, Boolean type) {
        String sql = "SELECT COUNT(*) AS count FROM moovieListsContent WHERE moovieListId = ?";
        Object[] params;

        if (type != null) {
            sql += " AND moovieListsContent.mediaId IN (SELECT mediaId FROM media WHERE type = ?)";
            params = new Object[]{moovieListId, type};
        } else {
            params = new Object[]{moovieListId};
        }

        return jdbcTemplate.query(sql, params, COUNT_ROW_MAPPER).stream().findFirst();
    }



    public int userHasListWithName(int userId, String name){
        Optional<MoovieList> r = jdbcTemplate.query("SELECT * FROM moovieLists WHERE name = ? AND userId= ? ",new Object[]{name,userId},  MOOVIE_LIST_ROW_MAPPER).stream().findFirst();
        if(r.isPresent()){
            return r.get().getMoovieListId();
        }
        return -1;
    }

    @Override
    public List<MoovieList> getAllStandardPublicMoovieListFromUser(int userId, int size, int pageNumber) {
        return jdbcTemplate.query("SELECT * FROM moovieLists WHERE userId = ?  LIMIT ? OFFSET ?",new Object[]{userId, size, pageNumber * size} , MOOVIE_LIST_ROW_MAPPER);
    }

    @Override
    public List<MoovieList> getMoovieListBySearch(String searchString, int size, int pageNumber){
        return jdbcTemplate.query("SELECT * FROM moovieLists WHERE moovieLists.name ILIKE ? LIMIT ? OFFSET ?", new Object[]{'%' + searchString + '%', size, pageNumber * size}, MOOVIE_LIST_ROW_MAPPER);
    }

    @Override
    public List<MoovieList> getMoovieListDefaultPrivateFromUser(int userId) {
        return jdbcTemplate.query("SELECT * FROM moovieLists WHERE userId = ? AND type = ?",new Object[]{userId, MOOVIE_LIST_TYPE_DEFAULT_PRIVATE} , MOOVIE_LIST_ROW_MAPPER);
    }


    /**
     * INSERTS INTO MOOVIE LISTS
     */

    @Override
    public MoovieList createMoovieList(int userId, String name, int type, String description) {
        int auxId = userHasListWithName(userId,name);
        if(auxId>0){
            String sqlDelContent = "DELETE FROM moovieListsContent WHERE moovieListId = " +auxId ;
            String sqlDel = "DELETE FROM moovieLists WHERE moovieListId = " +auxId ;

            jdbcTemplate.execute(sqlDel);
        }

        final Map<String, Object> args = new HashMap<>();
        args.put("userId", userId);
        args.put("name", name);
        args.put("description", description);
        args.put("type", type);

        final Number moovieListId = moovieListJdbcInsert.executeAndReturnKey(args);
        return new MoovieList(moovieListId.intValue(), userId, name, description, type);
    }

    @Override
    public void deleteMoovieList(int userId, int mediaIdList) {

    }

    @Override
    public MoovieList createMoovieListWithContent(int userId, String name, int type, String description, List<Integer> mediaIdList) {
        final MoovieList mL = createMoovieList(userId,name,type,description);

        return insertMediaIntoMoovieList(mL.getMoovieListId(), mediaIdList);
    }

    @Override
    public MoovieList insertMediaIntoMoovieList(int moovieListid, List<Integer> mediaIdList) {
        boolean repeatedElements = ( mediaIdList.stream().distinct().count() != mediaIdList.size() );
        if(repeatedElements){
            //TODO
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



    /***
     * QUERYS RELATED TO LIKES
     * */

    @Override
    public Optional<Integer> getLikesCount(int moovieListId) {
        return jdbcTemplate.query("SELECT COUNT(*) AS count FROM moovieListsLikes WHERE moovieListId = ?", new Object[]{moovieListId} , COUNT_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public List<User> getAllUsersWhoLikedMoovieList(int moovieListId) {
        return null;
    }

    private Optional<MoovieListLikes> getMoovieListLikes(int userId, int moovieListId){
        return jdbcTemplate.query("SELECT * FROM moovieListsLikes WHERE moovieListId = ? AND userId = ?", new Object[]{moovieListId, userId} , MOOVIE_LIST_LIKES_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public MoovieListLikes likeMoovieList(int userId, int moovieListId) {
        final Map<String,Object> args = new HashMap<>();
        args.put("moovieListId", moovieListId);
        args.put("userId", userId);

        moovieListLikesJdbcInsert.execute(args);

        return getMoovieListLikes(userId, moovieListId).get();
    }

    @Override
    public MoovieListLikes removeLikeMoovieList(int userId, int moovieListId) {
        String sql = "DELETE FROM moovielistslikes WHERE userid=? AND moovieListId = ?";
        jdbcTemplate.update( sql , new Object[]{userId, moovieListId} );
        return null;
    }

    @Override
    public boolean likeMoovieListStatusForUser(int userId, int moovieListId) {
        Optional<MoovieListLikes> aux = getMoovieListLikes(userId, moovieListId);
        if(aux.isPresent()){
            return true;
        }
        return false;
    }

    @Override
    public List<MoovieList> likedMoovieListsForUser(int userId, int size, int pageNumber) {
        return jdbcTemplate.query("SELECT moovieLists.moovieListId, moovieLists.name, moovieLists.name, moovieLists.description FROM moovieLists " +
                "WHERE moovieLists.moovieListId IN (SELECT moovieListsLikes.moovieListId FROM moovieListsLikes WHERE userId = ?) LIMIT ? OFFSET ?",
                new Object[]{userId, size, pageNumber*size } , MOOVIE_LIST_ROW_MAPPER);
    }


}


