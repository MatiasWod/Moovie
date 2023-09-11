package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Genre.Genre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Optional;

@Repository
public class GenreDaoJdbcImpl implements GenreDao{
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert genrejdbcInsert;

    private static final RowMapper<Genre> GENRE_ROW_MAPPER = (rs, rowNum) -> new Genre(
            rs.getInt("mediaId"),
            rs.getString("genre")
    );

    private static final RowMapper<Integer> COUNT_ROW_MAPPER = ((resultSet, i) -> resultSet.getInt("count"));

    @Autowired
    public GenreDaoJdbcImpl(final DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
        genrejdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("genres").usingGeneratedKeyColumns("mediaId");
        jdbcTemplate.execute(
                "CREATE TABLE IF NOT EXISTS genres(" +
                        "mediaId                   INTEGER NOT NULL," +
                        "genre                   VARCHAR(100) NOT NULL," +
                        "PRIMARY KEY(mediaId,genre)," +
                        "FOREIGN KEY(mediaId)       REFERENCES media(mediaId) ON DELETE CASCADE)");
    }

    @Override
    public Optional<Genre> getGenreForMedia(int mediaId) {
        //revisar el findFirst, creo que siempre devuelve el primer género que encuentre que matchea con el tvId
        return jdbcTemplate.query("SELECT * FROM media WHERE mediaId = ?",new Object[]{mediaId},GENRE_ROW_MAPPER).stream().findFirst();
    }
}
