package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.exceptions.UnableToInsertIntoDatabase;
import ar.edu.itba.paw.models.Media.MediaTypes;
import ar.edu.itba.paw.models.MoovieList.*;
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
    private final SimpleJdbcInsert moovieListFollowsJdbcInsert;
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

    private static final RowMapper<MoovieListFollowers> MOOVIE_LIST_FOLLOWERS_ROW_MAPPER = (rs, rowNum) -> new MoovieListFollowers(
            rs.getInt("moovielistid"),
            rs.getInt("userId")
    );

    private static final RowMapper<MoovieListCard> MOOVIE_LIST_CARD_ROW_MAPPER = (rs, rowNum) -> new MoovieListCard(
        rs.getInt("moovieListId"),
        rs.getString("name"),
        rs.getString("username"),
        rs.getString("description"),
        rs.getInt("likeCount"),
        rs.getBoolean("currentUserHasLiked"),
        rs.getInt("followerCount"),
        rs.getBoolean("currentUserHasFollowed"),
        rs.getInt("type"),
        rs.getInt("size"),
        rs.getInt("movieAmount"),
        rs.getInt("currentUserWatchAmount"),
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
            rs.getBoolean("isWatched"),
            rs.getString("genres"),
            rs.getString("providers")
    );

    private static final RowMapper<Integer> COUNT_ROW_MAPPER = ((resultSet, i) -> resultSet.getInt("count"));

    private static final RowMapper<Integer> MEDIAID_LIST_ROWMAPPER = ((resultSet, i) -> resultSet.getInt("mediaId"));


    public MoovieListDaoJdbcImpl(final DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
        moovieListJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("moovieLists").usingGeneratedKeyColumns("moovielistid");
        moovieListContentJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("moovieListsContent");
        moovieListLikesJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("moovieListsLikes");
        moovieListFollowsJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("moovieListsFollows");
    }

    @Override
    public Optional<MoovieList> getMoovieListById(int moovieListId) {
        return jdbcTemplate.query("SELECT * FROM moovieLists WHERE moovieListId = ?",new Object[]{moovieListId},MOOVIE_LIST_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public Optional<MoovieListCard> getMoovieListCardById(int moovieListId, int currentUserId) {
        StringBuilder sql = new StringBuilder("SELECT ml.*, u.username, COUNT(l.userid) AS likeCount, COUNT(mlf.userid) AS followerCount,");
        ArrayList<Object> args = new ArrayList<>();

        sql.append(" ( CASE WHEN EXISTS (SELECT 1 FROM moovielistslikes mll WHERE mll.moovieListId = ? AND mll.userId = ?) ");
        sql.append(" THEN true ELSE false END ) AS currentUserHasLiked, ");
        args.add(moovieListId);
        args.add(currentUserId);

        sql.append(" ( CASE WHEN EXISTS (SELECT 1 FROM moovielistsfollows mlf1 WHERE mlf1.moovieListId = ? AND mlf1.userId = ?) ");
        sql.append(" THEN true ELSE false END ) AS currentUserHasFollowed, ");
        args.add(moovieListId);
        args.add(currentUserId);

        sql.append(" ( SELECT ARRAY_AGG(posterPath) FROM ( SELECT m.posterPath FROM moovielistscontent mlc INNER JOIN media m ");
        sql.append(" ON mlc.mediaId = m.mediaId WHERE mlc.moovielistId = ml.moovielistid LIMIT 4 ) AS subquery ) AS images, ");
        sql.append(" (SELECT COUNT(*) FROM moovieListsContent mlc2 WHERE mlc2.moovieListId = ml.moovieListId) AS size, ");
        sql.append(" (SELECT COUNT(*) FROM moovielistsContent mlc3 INNER JOIN media m2 ON mlc3.mediaid = m2.mediaid WHERE m2.type = false AND mlc3.moovieListid = ml.moovieListId) AS movieAmount, ");

        sql.append(" (SELECT COUNT(*) FROM moovielistsContent mlc4 INNER JOIN moovielistscontent mlc5 on mlc4.mediaid=mlc5.mediaid JOIN moovieLists ml2 ");
        sql.append(" ON mlc5.moovieListId = ml2.moovieListId WHERE mlc4.moovieListId = ? AND ml2.name = 'Watched' AND ml2.userId = ?) AS currentUserWatchAmount ");
        args.add(moovieListId);
        args.add(currentUserId);


        sql.append(" FROM moovieLists ml LEFT JOIN users u ON ml.userid = u.userid LEFT JOIN moovieListsLikes l ON ml.moovielistid = l.moovielistid ");
        sql.append(" LEFT JOIN moovieListsFollows mlf ON ml.moovielistid = mlf.moovielistid ");
        sql.append(" WHERE ml.moovieListId = ? GROUP BY ml.moovielistid, u.userid;");

        args.add(moovieListId);

        return jdbcTemplate.query(sql.toString(), args.toArray(), MOOVIE_LIST_CARD_ROW_MAPPER).stream().findFirst();
    }


    @Override
    public List<MoovieListCard> getMoovieListCards(String search, String ownerUsername , int type , String orderBy, String order, int size, int pageNumber, int currentUserId){
        StringBuilder sql = new StringBuilder("SELECT ml.*, u.username, COUNT(l.userid) AS likeCount, COUNT(mlf.userid) AS followerCount, ");
        ArrayList<Object> args = new ArrayList<>();

        sql.append(" ( CASE WHEN EXISTS (SELECT 1 FROM moovielistslikes mll WHERE mll.moovieListId = ml.moovieListId AND mll.userId = ?) ");
        sql.append(" THEN true ELSE false END )AS currentUserHasLiked, ");
        args.add(currentUserId);

        sql.append(" ( CASE WHEN EXISTS (SELECT 1 FROM moovielistsfollows mlfs WHERE mlfs.moovieListId = ml.moovieListId AND mlfs.userId = ?) ");
        sql.append(" THEN true ELSE false END )AS currentUserHasFollowed, ");
        args.add(currentUserId);

        sql.append(" ( SELECT ARRAY_AGG(posterPath) FROM ( SELECT posterPath FROM moovielistscontent mlc INNER JOIN media m ");
        sql.append(" ON mlc.mediaId = m.mediaid WHERE mlc.moovielistId = ml.moovielistid LIMIT 4 ) AS subquery ) AS images, ");
        sql.append(" (SELECT COUNT(*) FROM moovieListsContent mlc2 WHERE mlc2.moovieListId = ml.moovieListId) AS size, ");
        sql.append(" (SELECT COUNT(*) FROM moovielistsContent mlc3 INNER JOIN media m2 ON mlc3.mediaid = m2.mediaid WHERE m2.type = false AND mlc3.moovieListid = ml.moovieListId) AS movieAmount, ");

        sql.append(" (SELECT COUNT(*) FROM moovielistsContent mlc4 INNER JOIN moovielistscontent mlc5 on mlc4.mediaid=mlc5.mediaid JOIN moovieLists ml2 ");
        sql.append(" ON mlc5.moovieListId = ml2.moovieListId WHERE mlc4.moovieListId = ml.moovieListid AND ml2.name = 'Watched' AND ml2.userId = ?) AS currentUserWatchAmount ");
        args.add(currentUserId);

        sql.append(" FROM moovieLists ml LEFT JOIN users u ON ml.userid = u.userid LEFT JOIN moovieListsLikes l ON ml.moovielistid = l.moovielistid ");
        sql.append(" LEFT JOIN moovieListsFollows mlf ON ml.moovieListId = mlf.moovieListId ");

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

        sql.append(" GROUP BY ml.moovielistid, u.userid ");
        if(orderBy != null && orderBy.length() > 0){
            sql.append(" ORDER BY ").append(orderBy).append(" ").append(order);
        }
        sql.append(" LIMIT ? OFFSET ? ;");
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
    public List<MoovieListCard> getLikedMoovieListCards(int userId, int type, int size, int pageNumber, int currentUserId) {
        StringBuilder sql = new StringBuilder("SELECT ml.*, u.username, COUNT(l.userId) AS likeCount, COUNT(mlf1.userid) AS followerCount,  ");
        ArrayList<Object> args = new ArrayList<>();

        sql.append(" ( CASE WHEN EXISTS (SELECT 1 FROM moovielistslikes mll WHERE mll.moovieListId = ml.moovieListId AND mll.userId = ?) ");
        sql.append(" THEN true ELSE false END ) AS currentUserHasLiked, ");
        args.add(currentUserId);

        sql.append(" ( CASE WHEN EXISTS (SELECT 1 FROM moovielistsfollows mlfs WHERE mlfs.moovieListId = ml.moovieListId AND mlfs.userId = ?) ");
        sql.append(" THEN true ELSE false END ) AS currentUserHasFollowed, ");
        args.add(currentUserId);

        sql.append(" ( SELECT ARRAY_AGG(posterPath) FROM ( SELECT posterPath FROM moovieListsContent mlc INNER JOIN media m ");
        sql.append(" ON mlc.mediaId = m.mediaId WHERE mlc.moovieListId = ml.moovieListId LIMIT 4 ) AS subquery ) AS images, ");
        sql.append(" (SELECT COUNT(*) FROM moovieListsContent mlc2 WHERE mlc2.moovieListId = ml.moovieListId) AS size, ");
        sql.append(" (SELECT COUNT(*) FROM moovieListsContent mlc3 INNER JOIN media m2 ON mlc3.mediaId = m2.mediaId WHERE m2.type = false AND mlc3.moovieListId = ml.moovieListId) AS movieAmount, ");

        sql.append(" (SELECT COUNT(*) FROM moovielistsContent mlc4 INNER JOIN moovielistscontent mlc5 on mlc4.mediaid=mlc5.mediaid JOIN moovieLists ml2 ");
        sql.append(" ON mlc5.moovieListId = ml2.moovieListId WHERE mlc4.moovieListId = ml.moovieListid AND ml2.name = 'Watched' AND ml2.userId = ?) AS currentUserWatchAmount ");
        args.add(currentUserId);

        sql.append(" FROM moovieLists ml LEFT JOIN users u ON ml.userId = u.userId LEFT JOIN moovieListsLikes l ON ml.moovieListId = l.moovieListId ");
        sql.append(" LEFT JOIN moovieListsFollows mlf1 ON ml.moovieListId = mlf1.moovieListId ");

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
    public List<MoovieListCard> getFollowedMoovieListCards(int userId, int type, int size, int pageNumber, int currentUserId) {
        StringBuilder sql = new StringBuilder("SELECT ml.*, u.username, COUNT(l.userId) AS likeCount, COUNT(mlf1.userid) AS followerCount,  ");
        ArrayList<Object> args = new ArrayList<>();

        sql.append(" ( CASE WHEN EXISTS (SELECT 1 FROM moovielistslikes mll WHERE mll.moovieListId = ml.moovieListId AND mll.userId = ?) ");
        sql.append(" THEN true ELSE false END ) AS currentUserHasLiked, ");
        args.add(currentUserId);

        sql.append(" ( CASE WHEN EXISTS (SELECT 1 FROM moovielistsfollows mlfs WHERE mlfs.moovieListId = ml.moovieListId AND mlfs.userId = ?) ");
        sql.append(" THEN true ELSE false END ) AS currentUserHasFollowed, ");
        args.add(currentUserId);

        sql.append(" ( SELECT ARRAY_AGG(posterPath) FROM ( SELECT posterPath FROM moovieListsContent mlc INNER JOIN media m ");
        sql.append(" ON mlc.mediaId = m.mediaId WHERE mlc.moovieListId = ml.moovieListId LIMIT 4 ) AS subquery ) AS images, ");
        sql.append(" (SELECT COUNT(*) FROM moovieListsContent mlc2 WHERE mlc2.moovieListId = ml.moovieListId) AS size, ");
        sql.append(" (SELECT COUNT(*) FROM moovieListsContent mlc3 INNER JOIN media m2 ON mlc3.mediaId = m2.mediaId WHERE m2.type = false AND mlc3.moovieListId = ml.moovieListId) AS movieAmount, ");

        sql.append(" (SELECT COUNT(*) FROM moovielistsContent mlc4 INNER JOIN moovielistscontent mlc5 on mlc4.mediaid=mlc5.mediaid JOIN moovieLists ml2 ");
        sql.append(" ON mlc5.moovieListId = ml2.moovieListId WHERE mlc4.moovieListId = ml.moovieListid AND ml2.name = 'Watched' AND ml2.userId = ?) AS currentUserWatchAmount ");
        args.add(currentUserId);

        sql.append(" FROM moovieLists ml LEFT JOIN users u ON ml.userId = u.userId LEFT JOIN moovieListsLikes l ON ml.moovieListId = l.moovieListId ");
        sql.append(" LEFT JOIN moovieListsFollows mlf1 ON ml.moovieListId = mlf1.moovieListId ");

        sql.append(" WHERE type = ? ");
        args.add(type);

        sql.append(" AND ml.moovieListId IN (SELECT moovieListId FROM moovieListsFollows WHERE userId = ?) ");
        args.add(userId);

        sql.append(" GROUP BY ml.moovieListId, u.userId LIMIT ? OFFSET ?;");
        args.add(size);
        args.add(size * pageNumber);

        return jdbcTemplate.query(sql.toString(), args.toArray(), MOOVIE_LIST_CARD_ROW_MAPPER);
    }

    @Override
    public int getFollowedMoovieListCardsCount(int userId, int type) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) ");
        ArrayList<Object> args = new ArrayList<>();

        sql.append(" FROM moovieLists ml ");

        sql.append(" WHERE type = ? ");
        args.add(type);

        sql.append(" AND ml.moovieListId IN (SELECT moovieListId FROM moovieListsFollows WHERE userId = ?) ");
        args.add(userId);

        return jdbcTemplate.query(sql.toString(), args.toArray(), COUNT_ROW_MAPPER).stream().findFirst().get().intValue();
    }


    @Override
    public List<MoovieListCard> getRecommendedMoovieListCards(int moovieListId, int size, int pageNumber, int currentUserId){
        StringBuilder sql = new StringBuilder("SELECT ml.*, u.username, COUNT(l.userid) AS likeCount, COUNT(mlf.userid) AS followerCount,");
        ArrayList<Object> args = new ArrayList<>();

        sql.append(" ( CASE WHEN EXISTS (SELECT 1 FROM moovielistslikes mll WHERE mll.moovieListId = ? AND mll.userId = ?) ");
        sql.append(" THEN true ELSE false END ) AS currentUserHasLiked, ");
        args.add(moovieListId);
        args.add(currentUserId);

        sql.append(" ( CASE WHEN EXISTS (SELECT 1 FROM moovielistsfollows mlf1 WHERE mlf1.moovieListId = ? AND mlf1.userId = ?) ");
        sql.append(" THEN true ELSE false END ) AS currentUserHasFollowed, ");
        args.add(moovieListId);
        args.add(currentUserId);

        sql.append(" ( SELECT ARRAY_AGG(posterPath) FROM ( SELECT m.posterPath FROM moovielistscontent mlc INNER JOIN media m ");
        sql.append(" ON mlc.mediaId = m.mediaId WHERE mlc.moovielistId = ml.moovielistid LIMIT 4 ) AS subquery ) AS images, ");
        sql.append(" (SELECT COUNT(*) FROM moovieListsContent mlc2 WHERE mlc2.moovieListId = ml.moovieListId) AS size, ");
        sql.append(" (SELECT COUNT(*) FROM moovielistsContent mlc3 INNER JOIN media m2 ON mlc3.mediaid = m2.mediaid WHERE m2.type = false AND mlc3.moovieListid = ml.moovieListId) AS movieAmount, ");

        sql.append(" (SELECT COUNT(*) FROM moovielistsContent mlc4 INNER JOIN moovielistscontent mlc5 on mlc4.mediaid=mlc5.mediaid JOIN moovieLists ml2 ");
        sql.append(" ON mlc5.moovieListId = ml2.moovieListId WHERE mlc4.moovieListId = ? AND ml2.name = 'Watched' AND ml2.userId = ?) AS currentUserWatchAmount, ");
        args.add(moovieListId);
        args.add(currentUserId);

        sql.append(" COUNT(l.userid) AS totalLikes ");

        sql.append(" FROM moovieLists ml LEFT JOIN users u ON ml.userid = u.userid LEFT JOIN moovieListsLikes l ON ml.moovielistid = l.moovielistid ");
        sql.append(" LEFT JOIN moovielistsfollows mlf ON mlf.moovielistid = ml.moovielistid ");
        sql.append(" WHERE type = 1  AND l.userid IN (SELECT userid FROM moovielistslikes WHERE moovielistid = ?) AND ml.moovielistid <> ? ");
        args.add(moovieListId);
        args.add(moovieListId);

        sql.append(" GROUP BY ml.moovielistid, u.userid  ORDER BY totallikes desc LIMIT ? OFFSET ? ; ");
        args.add(size);
        args.add(pageNumber*size);

        return jdbcTemplate.query(sql.toString(), args.toArray(), MOOVIE_LIST_CARD_ROW_MAPPER);
    }

    @Override
    public List<MoovieListContent> getMoovieListContent(int moovieListId, int userid , String orderBy, String sortOrder ,int size, int pageNumber){
        StringBuilder sql = new StringBuilder("SELECT m.*, mlc.*,  ");
        ArrayList<Object> args = new ArrayList<>();

        //Add the part of the query that checks if its watched by its owner
        sql.append(" (CASE WHEN EXISTS ( SELECT 1 FROM moovielists ml INNER JOIN moovieListsContent mlc2 ON ml.moovielistid = mlc2.moovielistid  ");
        sql.append(" WHERE m.mediaId = mlc2.mediaId AND ml.name = 'Watched' AND ml.userid = ? ) THEN true ELSE false END) AS isWatched,");
        sql.append("(SELECT ARRAY_AGG(g.genre) FROM genres g WHERE g.mediaId = m.mediaId) AS genres, ");
        sql.append("(SELECT ARRAY_AGG(p) FROM providers p WHERE p.mediaId = m.mediaId) AS providers, ");
        sql.append("AVG(r.rating) AS totalRating, COUNT(r.rating) AS votecount ");

        sql.append(" FROM moovieListsContent mlc ");
        args.add(userid);
        sql.append(" INNER JOIN media m ON mlc.mediaId = m.mediaId LEFT JOIN reviews r ON m.mediaid = r.mediaid ");

        sql.append(" WHERE mlc.moovielistid = ? ");
        args.add(moovieListId);

        sql.append(" GROUP BY m.mediaId, mlc.moovielistid, mlc.mediaId, mlc.customorder ");

        if(orderBy!=null && !orderBy.isEmpty() ){
            sql.append(" ORDER BY ").append(orderBy).append(" ").append(sortOrder);
        }
        sql.append(" LIMIT ? OFFSET ? ;");
        args.add(size);
        args.add(pageNumber*size);

        // Execute the query
        return jdbcTemplate.query(sql.toString(), args.toArray(), MOOVIE_LIST_CONTENT_ROW_MAPPER);
    }


    @Override
    public List<MoovieListContent> getFeaturedMoovieListContent(int moovieListId, int mediaType, int userid ,String featuredListOrder, String orderBy, String sortOrder ,int size, int pageNumber){
        StringBuilder sql = new StringBuilder("SELECT * FROM (SELECT m.*,  ");

        ArrayList<Object> args = new ArrayList<>();

        //Add the part of the query that checks if its watched by its owner
        sql.append(" (CASE WHEN EXISTS ( SELECT 1 FROM moovielists ml INNER JOIN moovieListsContent mlc2 ON ml.moovielistid = mlc2.moovielistid  ");
        sql.append(" WHERE m.mediaId = mlc2.mediaId AND ml.name = 'Watched' AND ml.userid = ? ) THEN true ELSE false END) AS isWatched, ");
        sql.append("(SELECT ARRAY_AGG(g.genre) FROM genres g WHERE g.mediaId = m.mediaId) AS genres, ");
        sql.append("(SELECT ARRAY_AGG(p) FROM providers p WHERE p.mediaId = m.mediaId) AS providers, ");
        sql.append("AVG(rating) AS totalRating, COUNT(rating) AS votecount ");

        sql.append("FROM media m LEFT JOIN reviews r ON m.mediaid = r.mediaid");
        args.add(userid);
        // If type is 0 or 1 it's specifically movies or TVs, else it's not restricted
        if (mediaType == MediaTypes.TYPE_MOVIE.getType() || mediaType == MediaTypes.TYPE_TVSERIE.getType()) {
            sql.append(" WHERE type = ? ");
            args.add(mediaType == MediaTypes.TYPE_TVSERIE.getType());
        } else {
            sql.append(" WHERE type IS NOT NULL ");
        }

        sql.append(" GROUP BY m.mediaId ");

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
    public int countWatchedFeaturedMoovieListContent(int moovieListId, int mediaType, int userid , String featuredListOrder, String orderBy, String sortOrder, int size, int pageNumber) {
        StringBuilder sql = new StringBuilder(" SELECT COUNT(*) FROM (SELECT * FROM (SELECT *,  ");

        ArrayList<Object> args = new ArrayList<>();

        //Add the part of the query that checks if its watched by its owner
        sql.append(" (CASE WHEN EXISTS ( SELECT 1 FROM moovielists ml INNER JOIN moovieListsContent mlc2 ON ml.moovielistid = mlc2.moovielistid  ");
        sql.append(" WHERE m.mediaId = mlc2.mediaId AND ml.name = 'Watched' AND ml.userid = ? ) THEN true ELSE false END) AS isWatched, ");
        sql.append("(SELECT ARRAY_AGG(g.genre) FROM genres g WHERE g.mediaId = m.mediaId) AS genres, ");
        sql.append("(SELECT ARRAY_AGG(p.providerName) FROM providers p WHERE p.mediaId = m.mediaId) AS providerNames, ");
        sql.append("(SELECT ARRAY_AGG(p.logoPath) FROM providers p WHERE p.mediaId = m.mediaId) AS providerLogos ");
        sql.append("FROM media m ");
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
        sql.append(" LIMIT ? OFFSET ?) AS featured WHERE featured.isWatched = true ;");
        args.add(size);
        args.add(pageNumber*size);

        // Execute the query
        return jdbcTemplate.query(sql.toString(), args.toArray(), COUNT_ROW_MAPPER).stream().findFirst().get().intValue();
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

        int currentMaxOrder = jdbcTemplate.queryForObject("SELECT COALESCE(MAX(customorder),0) FROM moovielistscontent WHERE moovielistid = ?", new Object[]{moovieListid}, Integer.class);

        // Iterate through the mediaIdList and insert each mediaId
        for (Integer mediaId : mediaIdList) {
            currentMaxOrder++;
            final Map<String, Object> args = new HashMap<>();
            args.put("moovieListId", moovieListid);
            args.put("mediaId", mediaId);
            args.put("customOrder", currentMaxOrder);

            try {
                moovieListContentJdbcInsert.execute(args);
            } catch (DataIntegrityViolationException e) {
                throw new UnableToInsertIntoDatabase("Unable to insert media into the MoovieList, would result in repeated elements");
            }
        }

        // Return the MoovieList (assuming it exists)
        return getMoovieListById(moovieListid).orElse(null);
    }

    @Override
    public void deleteMediaFromMoovieList(int moovieListId, int mediaId){
        String sqlDel = "DELETE FROM moovieListsContent " +
                " WHERE moovieListId = " + moovieListId + " AND moovieListsContent.mediaId = " + mediaId ;
        jdbcTemplate.execute(sqlDel);
    }

    @Override
    public void updateMoovieListOrder(int moovieListId, int[] toPrevPage, int[] currentPage, int[] toNextPage) {
        return;
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
    public void removeLikeMoovieList(int userId, int moovieListId) {
        String sql = "DELETE FROM moovielistslikes WHERE userid=? AND moovieListId = ?";
        jdbcTemplate.update( sql , new Object[]{userId, moovieListId} );
    }


    @Override
    public void removeFollowMoovieList(int userId, int moovieListId) {
        String sql = "DELETE FROM moovielistsfollows WHERE userid=? AND moovieListId = ?";
        jdbcTemplate.update( sql , new Object[]{userId, moovieListId} );
    }

    @Override
    public void followMoovieList(int userId, int moovieListId) {
        final Map<String,Object> args = new HashMap<>();
        args.put("moovieListId", moovieListId);
        args.put("userId", userId);

        moovieListFollowsJdbcInsert.execute(args);
    }
}


