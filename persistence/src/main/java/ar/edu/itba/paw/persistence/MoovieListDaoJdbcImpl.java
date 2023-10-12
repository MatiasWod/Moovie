package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.exceptions.UnableToInsertIntoDatabase;
import ar.edu.itba.paw.models.Media.MediaTypes;
import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.models.MoovieList.MoovieListCard;
import ar.edu.itba.paw.models.MoovieList.MoovieListContent;
import ar.edu.itba.paw.models.MoovieList.MoovieListLikes;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;

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


    private static final RowMapper<MoovieListLikes> MOOVIE_LIST_LIKES_ROW_MAPPER = (rs, rowNum) -> new MoovieListLikes(
            rs.getInt("moovielistid"),
            rs.getInt("userId")
    );

    private static final RowMapper<MoovieListCard> MOOVIE_LIST_CARD_ROW_MAPPER = (rs, rowNum) -> new MoovieListCard(
        rs.getInt("moovieListId"),
        rs.getString("name"),
        rs.getString("username"),
        rs.getString("description"),
        rs.getInt("likeCount"),
        rs.getInt("type"),
        rs.getInt("size"),
        rs.getInt("movieAmount"),
        rs.getString("images")
    );

    private static final RowMapper<MoovieListContent> MOOVIE_LIST_CONTENT_ROW_MAPPER = (rs, rowNum) -> new MoovieListContent(
            rs.getInt("mediaId"),
            rs.getBoolean("type"),
            rs.getString("name"),
            rs.getString("originalLanguage"),
            rs.getBoolean("adult"),
            rs.getDate("releaseDate"),
            rs.getString("overview"),
            rs.getString("backdropPath"),
            rs.getString("posterPath"),
            rs.getString("trailerLink"),
            rs.getFloat("tmdbRating"),
            rs.getInt("totalRating"),
            rs.getInt("voteCount"),
            rs.getString("status"),
            rs.getBoolean("isWatched")
    );

    private static final RowMapper<Integer> COUNT_ROW_MAPPER = ((resultSet, i) -> resultSet.getInt("count"));

    private static final RowMapper<Integer> MEDIAID_LIST_ROWMAPPER = ((resultSet, i) -> resultSet.getInt("mediaId"));


    public MoovieListDaoJdbcImpl(final DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
        moovieListJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("moovieLists").usingGeneratedKeyColumns("moovielistid");
        moovieListContentJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("moovieListsContent");
        moovieListLikesJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("moovieListsLikes");
    }


    @Override
    public MoovieList getWatchedByUserId(int userId) {
        return null;
    }

    @Override
    public MoovieList getWatchlistByUserId(int userId) {
        return null;
    }

    @Override
    public Optional<MoovieList> getMoovieListById(int moovieListId) {
        return jdbcTemplate.query("SELECT * FROM moovieLists WHERE moovieListId = ?",new Object[]{moovieListId},MOOVIE_LIST_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public Optional<MoovieListCard> getMoovieListCardById(int moovieListId) {
        StringBuilder sql = new StringBuilder("SELECT ml.*, u.username, COUNT(l.userid) AS likeCount, ");
        ArrayList<Object> args = new ArrayList<>();

        sql.append(" ( SELECT ARRAY_AGG(posterPath) FROM ( SELECT m.posterPath FROM moovielistscontent mlc INNER JOIN media m ");
        sql.append(" ON mlc.mediaId = m.mediaId WHERE mlc.moovielistId = ml.moovielistid LIMIT 4 ) AS subquery ) AS images, ");
        sql.append(" (SELECT COUNT(*) FROM moovieListsContent mlc2 WHERE mlc2.moovieListId = ml.moovieListId) AS size, ");
        sql.append(" (SELECT COUNT(*) FROM moovielistsContent mlc3 INNER JOIN media m2 ON mlc3.mediaid = m2.mediaid WHERE m2.type = false AND mlc3.moovieListid = ml.moovieListId) AS movieAmount ");
        sql.append(" FROM moovieLists ml LEFT JOIN users u ON ml.userid = u.userid LEFT JOIN moovieListsLikes l ON ml.moovielistid = l.moovielistid ");
        sql.append(" WHERE ml.moovieListId = ? GROUP BY ml.moovielistid, u.userid;");

        args.add(moovieListId);

        return jdbcTemplate.query(sql.toString(), args.toArray(), MOOVIE_LIST_CARD_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public List<MoovieListCard> getMoovieListCards( String search, String ownerUsername , int type , int size, int pageNumber){
        StringBuilder sql = new StringBuilder("SELECT ml.*, u.username, COUNT(l.userid) AS likeCount, ");
        ArrayList<Object> args = new ArrayList<>();

        sql.append(" ( SELECT ARRAY_AGG(posterPath) FROM ( SELECT posterPath FROM moovielistscontent mlc INNER JOIN media m ");
        sql.append(" ON mlc.mediaId = m.mediaid WHERE mlc.moovielistId = ml.moovielistid LIMIT 4 ) AS subquery ) AS images, ");
        sql.append(" (SELECT COUNT(*) FROM moovieListsContent mlc2 WHERE mlc2.moovieListId = ml.moovieListId) AS size, ");
        sql.append(" (SELECT COUNT(*) FROM moovielistsContent mlc3 INNER JOIN media m2 ON mlc3.mediaid = m2.mediaid WHERE m2.type = false AND mlc3.moovieListid = ml.moovieListId) AS movieAmount ");
        sql.append(" FROM moovieLists ml LEFT JOIN users u ON ml.userid = u.userid LEFT JOIN moovieListsLikes l ON ml.moovielistid = l.moovielistid ");

        sql.append(" WHERE type = ? ");
        args.add(type);

        if(ownerUsername!=null && ownerUsername.length() > 0){
            sql.append(" AND u.username = ? ");
            args.add(ownerUsername);
        }
        if(search != null && search.length() > 0){
            sql.append(" AND name ILIKE ? ");
            args.add('%' + search + '%');
        }

        sql.append(" GROUP BY ml.moovielistid, u.userid LIMIT ? OFFSET ? ; ");
        args.add(size);
        args.add(size*pageNumber);

        // Execute the query
        return jdbcTemplate.query(sql.toString(), args.toArray(), MOOVIE_LIST_CARD_ROW_MAPPER);
    }

    @Override
    public int getMoovieListCardsCount(String search, String ownerUsername , int type , int size, int pageNumber){
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM moovielists");
        ArrayList<Object> args = new ArrayList<>();

        sql.append(" WHERE type = ? ");
        args.add(type);

        if(search != null && search.length() > 0){
            sql.append(" AND name ILIKE ? ");
            args.add('%' + search + '%');
        }

        // Execute the query
        return jdbcTemplate.query(sql.toString(), args.toArray(), COUNT_ROW_MAPPER).stream().findFirst().get().intValue();
    }


    @Override
    public List<MoovieListCard> getLikedMoovieListCards(int userId, int type, int size, int pageNumber) {
        StringBuilder sql = new StringBuilder("SELECT ml.*, u.username, COUNT(l.userId) AS likeCount, ");
        ArrayList<Object> args = new ArrayList<>();

        sql.append(" ( SELECT ARRAY_AGG(posterPath) FROM ( SELECT posterPath FROM moovieListsContent mlc INNER JOIN media m ");
        sql.append(" ON mlc.mediaId = m.mediaId WHERE mlc.moovieListId = ml.moovieListId LIMIT 4 ) AS subquery ) AS images, ");
        sql.append(" (SELECT COUNT(*) FROM moovieListsContent mlc2 WHERE mlc2.moovieListId = ml.moovieListId) AS size, ");
        sql.append(" (SELECT COUNT(*) FROM moovieListsContent mlc3 INNER JOIN media m2 ON mlc3.mediaId = m2.mediaId WHERE m2.type = false AND mlc3.moovieListId = ml.moovieListId) AS movieAmount ");
        sql.append(" FROM moovieLists ml LEFT JOIN users u ON ml.userId = u.userId LEFT JOIN moovieListsLikes l ON ml.moovieListId = l.moovieListId ");

        sql.append(" WHERE type = ? ");
        args.add(type);

        sql.append(" AND ml.moovieListId IN (SELECT moovieListId FROM moovieListsLikes WHERE userId = ?) ");
        args.add(userId);

        sql.append(" GROUP BY ml.moovieListId, u.userId LIMIT ? OFFSET ?;");
        args.add(size);
        args.add(size * pageNumber);

        return jdbcTemplate.query(sql.toString(), args.toArray(), MOOVIE_LIST_CARD_ROW_MAPPER);
    }

    @Override
    public List<MoovieListContent> getMoovieListContent(int moovieListId, int userid , String orderBy, String sortOrder ,int size, int pageNumber){
        StringBuilder sql = new StringBuilder("SELECT *,  ");
        ArrayList<Object> args = new ArrayList<>();

        //Add the part of the query that checks if its watched by its owner
        sql.append(" (CASE WHEN EXISTS ( SELECT 1 FROM moovielists ml INNER JOIN moovieListsContent mlc2 ON ml.moovielistid = mlc2.moovielistid  ");
        sql.append(" WHERE m.mediaId = mlc2.mediaId AND ml.name = 'Watched' AND ml.userid = ? ) THEN true ELSE false END) AS isWatched FROM moovieListsContent mlc ");
        args.add(userid);
        sql.append(" INNER JOIN media m ON mlc.mediaId = m.mediaId  ");

        sql.append(" WHERE mlc.moovielistid = ? ");
        args.add(moovieListId);

        if(orderBy!=null && !orderBy.isEmpty() ){
            sql.append(" ORDER BY ").append(orderBy).append(" ").append(sortOrder);
        }
        sql.append(" LIMIT ? OFFSET ? ;");
        args.add(size);
        args.add(pageNumber*size);

        // Execute the query
        return jdbcTemplate.query(sql.toString(), args.toArray(), MOOVIE_LIST_CONTENT_ROW_MAPPER);
    }


    //realizar bien la query
    @Override
    public List<MoovieListContent> getFeaturedMoovieListContent(int moovieListId, int mediaType, int userid ,String featuredListOrder, String orderBy, String sortOrder ,int size, int pageNumber){
        StringBuilder sql = new StringBuilder("SELECT * FROM (SELECT *,  ");
        ArrayList<Object> args = new ArrayList<>();

        //Add the part of the query that checks if its watched by its owner
        sql.append(" (CASE WHEN EXISTS ( SELECT 1 FROM moovielists ml INNER JOIN moovieListsContent mlc2 ON ml.moovielistid = mlc2.moovielistid  ");
        sql.append(" WHERE m.mediaId = mlc2.mediaId AND ml.name = 'Watched' AND ml.userid = ? ) THEN true ELSE false END) AS isWatched FROM media m ");
        args.add(userid);
        // If type is 0 or 1 it's specifically movies or TVs, else it's not restricted
        if (mediaType == MediaTypes.TYPE_MOVIE.getType() || mediaType == MediaTypes.TYPE_TVSERIE.getType()) {
            sql.append(" WHERE type = ? ");
            args.add(mediaType == MediaTypes.TYPE_TVSERIE.getType());
        } else {
            sql.append(" WHERE type IS NOT NULL ");
        }
        if(orderBy!=null && !orderBy.isEmpty() ){
            sql.append(" ORDER BY ").append(featuredListOrder).append(" DESC LIMIT 100) AS topRated ORDER BY ").append(orderBy).append(" ").append(sortOrder);
        }
        sql.append(" LIMIT ? OFFSET ? ;");
        args.add(size);
        args.add(pageNumber*size);

        // Execute the query
        return jdbcTemplate.query(sql.toString(), args.toArray(), MOOVIE_LIST_CONTENT_ROW_MAPPER);
    }


    @Override
    public MoovieList createMoovieList(int userId, String name, int type, String description) {
        final Map<String, Object> args = new HashMap<>();
        args.put("userId", userId);
        args.put("name", name);
        args.put("description", description);
        args.put("type", type);

        try{
            final Number moovieListId = moovieListJdbcInsert.executeAndReturnKey(args);
            return new MoovieList(moovieListId.intValue(), userId, name, description, type);
        } catch(DuplicateKeyException e){
            throw new UnableToInsertIntoDatabase("Create MoovieList failed, already have a table with the given name");
        }
    }


    @Override
    public MoovieList insertMediaIntoMoovieList(int moovieListid, List<Integer> mediaIdList) {
        // Check for repeated elements
        boolean repeatedElements = mediaIdList.size() != mediaIdList.stream().distinct().count();
        if (repeatedElements) {
            throw new UnableToInsertIntoDatabase("Unable to insert into the MoovieList since there are repeated elements");
        }

        // Iterate through the mediaIdList and insert each mediaId
        for (Integer mediaId : mediaIdList) {
            final Map<String, Object> args = new HashMap<>();
            args.put("moovieListId", moovieListid);
            args.put("mediaId", mediaId);

            try {
                moovieListContentJdbcInsert.execute(args);
            } catch (DataIntegrityViolationException e) {
                throw new UnableToInsertIntoDatabase("Unable to insert into the MoovieList, would result in repeated elements");
            }
        }

        // Return the MoovieList (assuming it exists)
        return getMoovieListById(moovieListid).orElse(null);
    }

    @Override
    public void deleteMoovieList(int moovieListId) {
        String sqlDel = "DELETE FROM moovieLists WHERE moovieListId = " + moovieListId;
        jdbcTemplate.execute(sqlDel);
    }

    @Override
    public void likeMoovieList(int userId, int moovieListId) {
        final Map<String,Object> args = new HashMap<>();
        args.put("moovieListId", moovieListId);
        args.put("userId", userId);

        moovieListLikesJdbcInsert.execute(args);
    }

    @Override
    public int getLikeCountForMoovieList(int moovieListId){
        return jdbcTemplate.query("SELECT COUNT(*) FROM moovieListsLikes WHERE moovieListId = ?", new Object[]{moovieListId},COUNT_ROW_MAPPER)
                .stream().findFirst().get().intValue();
    }

    @Override
    public void removeLikeMoovieList(int userId, int moovieListId) {
        String sql = "DELETE FROM moovielistslikes WHERE userid=? AND moovieListId = ?";
        jdbcTemplate.update( sql , new Object[]{userId, moovieListId} );
    }

    @Override
    public boolean likeMoovieListStatusForUser(int userId, int moovieListId) {
        Optional<MoovieListLikes> mll = jdbcTemplate.query("SELECT * FROM moovieListsLikes WHERE moovieListId = ? AND userId = ?", new Object[]{moovieListId, userId} , MOOVIE_LIST_LIKES_ROW_MAPPER)
                .stream().findFirst();
        return mll.isPresent();
    }

    @Override
    public int countWatchedMoviesInList(int userId, int moovieListId){
        String sql = "SELECT COUNT(*) " +
                "FROM moovieListsContent mlc " +
                "JOIN moovieListsContent mlcw ON mlc.mediaId = mlcw.mediaId " +
                "JOIN moovieLists ml ON mlcw.moovieListId = ml.moovieListId " +
                "WHERE mlc.moovieListId = ? " +
                "AND ml.name = 'Watched' " +
                "AND ml.userId = ?; ";
        return jdbcTemplate.query(sql, new Object[]{moovieListId, userId}, COUNT_ROW_MAPPER).stream().findFirst().get().intValue();
    }
}


