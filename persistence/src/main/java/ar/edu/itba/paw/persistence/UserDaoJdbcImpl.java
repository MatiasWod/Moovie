package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.User.Image;
import ar.edu.itba.paw.models.User.Profile;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.models.User.UserRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;


@Repository
public class UserDaoJdbcImpl implements UserDao{
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert userJdbcInsert;
    private final SimpleJdbcInsert imageJdbcInsert;


    private static final RowMapper<User> USER_ROW_MAPPER = (rs, rowNum) -> new User(
            rs.getInt("userId"),
            rs.getString("username"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getInt("role")
    );

    private static final RowMapper<Profile> PROFILE_ROW_MAPPER = (rs, rowNum) -> new Profile(
            rs.getInt("userId"),
            rs.getString("username"),
            rs.getString("email"),
            rs.getInt("role"),
            rs.getInt("moovieListCount"),
            rs.getInt("likedMoovieListCount"),
            rs.getInt("reviewCount")
    );

    private static final RowMapper<Image> IMAGE_ROW_MAPPER = (rs, column) -> new Image(
            rs.getInt("userid"),
            rs.getBytes("image")
    );

    private static final RowMapper<Integer> COUNT_ROW_MAPPER = ((resultSet, i) -> resultSet.getInt("count"));

    @Autowired
    public UserDaoJdbcImpl(final DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
        userJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("users").usingGeneratedKeyColumns("userid");
        imageJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("userimages");
    }

    @Override
    public User createUser(String username, String email, String password) {
        final Map<String, Object> args = new HashMap<>();
        args.put("username",username);
        args.put("email", email);
        args.put("password",password);
        args.put("role", UserRoles.NOT_AUTHENTICATED.getRole());

        final Number reviewId = userJdbcInsert.executeAndReturnKey(args);
        return new User(reviewId.intValue(), username,email,password, UserRoles.NOT_AUTHENTICATED.getRole());
    }

    @Override
    public User createUserFromUnregistered(String username, String email, String password) {
        String sql = "UPDATE users SET username = ?, password = ?, role = ? WHERE email = ?";
        jdbcTemplate.update(sql, username, password, UserRoles.NOT_AUTHENTICATED.getRole(), email);
        return findUserByEmail(email).get();
    }

    @Override
    public void confirmRegister(int userId, int authenticated) {
        jdbcTemplate.update("UPDATE users SET role = ? WHERE userId = ?", authenticated, userId);
    }

    @Override
    public Optional<User> findUserById(int userId) {
        return jdbcTemplate.query("SELECT * FROM users WHERE userId = ?", new Object[]{userId}, USER_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return jdbcTemplate.query("SELECT * FROM users WHERE email ILIKE ?", new Object[]{email}, USER_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        return jdbcTemplate.query("SELECT * FROM users WHERE username ILIKE ?", new Object[]{username}, USER_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public List<Profile> searchUsers(String username, String orderBy, String sortOrder, int size, int pageNumber) {
        StringBuilder sql = new StringBuilder("SELECT * , ");
        ArrayList<Object> args = new ArrayList<>();

        sql.append(" (SELECT COUNT(*) FROM moovieLists ml WHERE ml.userId = u.userId) AS moovieListCount, ");
        sql.append(" (SELECT COUNT(*) FROM moovieListsLikes l WHERE l.userId = u.userId) AS likedMoovieListCount, ");
        sql.append(" (SELECT COUNT(*) FROM reviews r WHERE r.userId = u.userId) AS reviewCount ");
        sql.append(" FROM users u WHERE username ILIKE ? ");
        args.add('%' + username + '%');


        if(orderBy!=null && orderBy.length()>0 && sortOrder!=null && sortOrder.length()>0){
            sql.append(" ORDER BY ").append(orderBy).append(" ").append(sortOrder);
        }

        sql.append(" LIMIT ? OFFSET ? ");
        args.add(size);
        args.add(pageNumber*size);

        return jdbcTemplate.query(sql.toString(), args.toArray(), PROFILE_ROW_MAPPER);
    }

    @Override
    public int getSearchCount(String username) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) AS count ");
        ArrayList<Object> args = new ArrayList<>();

        sql.append("FROM users WHERE username ILIKE ? ");
        args.add('%' + username + '%');

        return jdbcTemplate.query(sql.toString(), args.toArray(), COUNT_ROW_MAPPER).stream().findFirst().get().intValue();
    }

    @Override
    public Optional<Profile> getProfileByUsername(String username) {
        StringBuilder sql = new StringBuilder("SELECT * , ");
        ArrayList<Object> args = new ArrayList<>();

        sql.append(" (SELECT COUNT(*) FROM moovieLists ml WHERE ml.userId = u.userId AND ml.type = 1) AS moovieListCount, ");
        sql.append(" (SELECT COUNT(*) FROM moovieListsLikes l WHERE l.userId = u.userId) AS likedMoovieListCount, ");
        sql.append(" (SELECT COUNT(*) FROM reviews r WHERE r.userId = u.userId) AS reviewCount ");
        sql.append(" FROM users u WHERE username = ?; ");
        args.add(username);

        return jdbcTemplate.query(sql.toString(), args.toArray(), PROFILE_ROW_MAPPER).stream().findFirst();
    }



    @Override
    public void setProfilePicture(int userId, byte[] image) {
        final Map<String, Object> args = new HashMap<>();
        args.put("image", image);
        args.put("userId", userId);

        imageJdbcInsert.execute(args);
    }

    @Override
    public boolean hasProfilePicture(int userId) {
        Optional<Integer> aux =  jdbcTemplate.query("SELECT COUNT(*) AS count FROM userImages WHERE userid = ? ", new Object[]{userId}, COUNT_ROW_MAPPER).stream().findFirst();
        if (aux.isPresent() && aux.get().intValue()>=1 ){
            return true;
        }
        return false;
    }

    @Override
    public void updateProfilePicture(int userId, byte[] image) {
        jdbcTemplate.update("UPDATE userImages SET image = ? WHERE userId = ?", new Object[]{image, userId});
    }

    @Override
    public Optional<Image> getProfilePicture(int userId) {
        return jdbcTemplate.query("SELECT * FROM userImages WHERE userid = ?", new Object[]{userId}, IMAGE_ROW_MAPPER).stream().findFirst();
    }


    /**
     * USER STATUS
     */

    @Override
    public void changeUserRole(int userId, int role) {
        jdbcTemplate.update("UPDATE users SET role = ? WHERE userId = ?", new Object[]{role, userId});
    }

    //Following functions needed in order to be safe of sql injection
    private boolean isOrderValid( String order) {
        String[] validOrders = {"username", "userid", "role", "moovieListCount", "likedMoovieListCount", "reviewCount"};
        for (String element : validOrders) {
            if (element.toLowerCase().equals(order)) {
                return true;
            }
        }
        return false;
    }
    private boolean isSortOrderValid(String so){
        if(so.toLowerCase().equals("asc") || so.toLowerCase().equals("desc")){
            return true;
        }
        return false;
    }
}
