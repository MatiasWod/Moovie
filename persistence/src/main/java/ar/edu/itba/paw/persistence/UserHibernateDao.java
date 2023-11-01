package ar.edu.itba.paw.persistence;


import ar.edu.itba.paw.models.User.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.sql.DataSource;
import java.util.*;

@Primary
@Repository
public class UserHibernateDao implements UserDao{

    @PersistenceContext
    private EntityManager entityManager;


    //Revisar, está mal así
    @Override
    public User createUser(String username, String email, String password) {
        final Map<String, Object> args = new HashMap<>();
        args.put("username",username);
        args.put("email", email);
        args.put("password",password);
        args.put("role", UserRoles.NOT_AUTHENTICATED.getRole());

        final User toCreateUser = new User.Builder(username,email,password,UserRoles.NOT_AUTHENTICATED.getRole()).build();
        entityManager.persist(toCreateUser);

        return toCreateUser;
    }

    @Override
    public User createUserFromUnregistered(String username, String email, String password) {
        entityManager.createNativeQuery("UPDATE users SET username = :username, password = :password, role = :role WHERE email = :email")
                .setParameter("username",username)
                .setParameter("password",password)
                .setParameter("role",UserRoles.NOT_AUTHENTICATED.getRole())
                .setParameter("email",email)
                .executeUpdate();
        return findUserByEmail(email).get();
    }

    @Override
    public void confirmRegister(int userId, int authenticated) {
        entityManager.createNativeQuery("UPDATE users SET role = :role WHERE userId = :userId")
                .setParameter("role",authenticated)
                .setParameter("userId",userId)
                .executeUpdate();
    }

    @Override
    public Optional<User> findUserById(int userId) {
        final TypedQuery<User> query = entityManager.createQuery("from User where userId = :userId",User.class);
        query.setParameter("userId",userId);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        final TypedQuery<User> query = entityManager.createQuery("from User where email = :email",User.class);
        query.setParameter("email",email);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        final TypedQuery<User> query = entityManager.createQuery("from User where username = (:username)",User.class);
        query.setParameter("username",username);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public List<Profile> searchUsers(String username, String orderBy, String sortOrder, int size, int pageNumber) {
        StringBuilder sql = new StringBuilder("SELECT * , ");
        sql.append(" (SELECT COUNT(*) FROM moovieLists ml WHERE ml.userId = u.userId) AS moovieListCount, ");
        sql.append(" (SELECT COUNT(*) FROM moovieListsLikes l WHERE l.userId = u.userId) AS likedMoovieListCount, ");
        sql.append(" (SELECT COUNT(*) FROM reviews r WHERE r.userId = u.userId) AS reviewCount ");
        sql.append(" FROM users u WHERE username ILIKE :username ");

        if(orderBy!=null && orderBy.length()>0 && sortOrder!=null && sortOrder.length()>0){
            sql.append(" ORDER BY ").append(orderBy).append(" ").append(sortOrder);
        }

        sql.append(" LIMIT :limit OFFSET :offset ");

        Query query = entityManager.createNativeQuery(sql.toString(),Profile.class)
                .setParameter("username",'%' + username + '%')
                .setParameter("limit",size)
                .setParameter("offset",size * pageNumber);

        return query.getResultList();
    }

    @Override
    public int getSearchCount(String username) {
        return ((Number) entityManager.createNativeQuery("SELECT COUNT(*) AS count FROM users WHERE username ILIKE :username")
                .setParameter("username",'%' + username + '%')
                .getSingleResult()).intValue();
    }

    @Override
    public Optional<Profile> getProfileByUsername(String username) {

        StringBuilder sql = new StringBuilder("SELECT u.userid,u.email,u.username,u.role, ");
        sql.append(" (SELECT COUNT(*) FROM moovieLists ml WHERE ml.userId = u.userId AND ml.type = 1) AS moovieListCount, ");
        sql.append(" (SELECT COUNT(*) FROM moovieListsLikes l WHERE l.userId = u.userId) AS likedMoovieListCount, ");
        sql.append(" (SELECT COUNT(*) FROM reviews r WHERE r.userId = u.userId) AS reviewCount ");
        sql.append(" FROM users u WHERE username = :username ");

        Query query = entityManager.createNativeQuery(sql.toString(),Profile.class).setParameter("username",username);

        return query.getResultList().stream().findFirst();
    }



    @Override
    public void setProfilePicture(int userId, byte[] image) {
        final Image toInsertImage = new Image(userId, image);
        entityManager.persist(toInsertImage);
    }

    @Override
    public boolean hasProfilePicture(int userId) {
        int aux = entityManager.createQuery("FROM Image WHERE userId = :userId")
                .setParameter("userId",userId)
                .getResultList().size();
        if (aux >= 1 ){
            return true;
        }
        return false;
    }

    @Override
    public void updateProfilePicture(int userId, byte[] image) {
        entityManager.createNativeQuery("UPDATE userImages SET image = :image WHERE userId = :userId")
                .setParameter("image",image)
                .setParameter("userId",userId)
                .executeUpdate();
    }

    @Override
    public Optional<Image> getProfilePicture(int userId) {
        final TypedQuery<Image> query = entityManager.createQuery("from Image where userId = :userId",Image.class);
        query.setParameter("userId",userId);
        return query.getResultList().stream().findFirst();
    }


    /**
     * USER STATUS
     */

    @Override
    public void changeUserRole(int userId, int role) {
        entityManager.createNativeQuery("UPDATE users SET role = :role WHERE userId = :userId")
                .setParameter("role",role)
                .setParameter("userId",userId)
                .executeUpdate();
    }

    //Following functions needed in order to be safe of sql injection
    private boolean isOrderValid( String order) {
        if(order==null || order.isEmpty()){
            return false;
        }
        order = order.replaceAll(" ","");
        String[] validOrders = {"username", "userid", "role", "moovieListCount", "likedMoovieListCount", "reviewCount"};
        for (String element : validOrders) {
            if (element.toLowerCase().equals(order)) {
                return true;
            }
        }
        return false;
    }
    private boolean isSortOrderValid(String so){
        if(so==null || so.isEmpty()){
            return false;
        }
        so = so.replaceAll(" ","");
        if(so.toLowerCase().equals("asc") || so.toLowerCase().equals("desc")){
            return true;
        }
        return false;
    }
}
