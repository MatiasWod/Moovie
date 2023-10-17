package ar.edu.itba.paw.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class GenreDaoJdbcImpl implements GenreDao{
    private final JdbcTemplate jdbcTemplate;


    private static final RowMapper<String> GENRE_STRING_ROW_MAPPER = (rs, rowNum) -> new String(
            rs.getString("genre")
    );

    @Autowired
    public GenreDaoJdbcImpl(final DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }



    @Override
        public List<String> getAllGenres() {
        return jdbcTemplate.query("SELECT DISTINCT genres.genre FROM genres ORDER BY genres.genre",GENRE_STRING_ROW_MAPPER);
    }

    @Override
    public List<String> getGenresForMedia(int mediaId){
        return jdbcTemplate.query("SELECT g.genre FROM genres g INNER JOIN media m on m.mediaId = g.mediaId WHERE m.mediaid = ? ", new Object[]{mediaId}, GENRE_STRING_ROW_MAPPER);
    }


}
