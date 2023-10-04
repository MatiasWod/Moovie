package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Genre.Genre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class GenreDaoJdbcImpl implements GenreDao{
    private final JdbcTemplate jdbcTemplate;


    private static final RowMapper<String> ALL_GENRES_ROW_MAPPER = (rs, rowNum) -> new String(
            rs.getString("genre")
    );

    @Autowired
    public GenreDaoJdbcImpl(final DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }



    @Override
        public List<String> getAllGenres() {
        //revisar el findFirst, creo que siempre devuelve el primer género que encuentre que matchea con el tvId

        return jdbcTemplate.query("SELECT DISTINCT genres.genre FROM genres",ALL_GENRES_ROW_MAPPER);
    }


}
