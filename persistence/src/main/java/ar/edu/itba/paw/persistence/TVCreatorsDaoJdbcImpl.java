package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.TV.TVCreators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Optional;

@Repository
public class TVCreatorsDaoJdbcImpl implements TVCreatorsDao{
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert tvCreatorsJdbcInsert;

    private static final RowMapper<TVCreators> TV_CREATORS_ROW_MAPPER = (rs, rowNum) -> new TVCreators(
            rs.getInt("mediaId"),
            rs.getInt("creatorId"),
            rs.getString("creatorName")
    );

    private static final RowMapper<Integer> COUNT_ROW_MAPPER = ((resultSet, i) -> resultSet.getInt("count"));

    @Autowired
    public TVCreatorsDaoJdbcImpl(final DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
        tvCreatorsJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("creators").usingGeneratedKeyColumns("mediaId");
        jdbcTemplate.execute(
                "CREATE TABLE IF NOT EXISTS creators(" +
                        "mediaId                            INTEGER NOT NULL," +
                        "creatorId                          INTEGER NOT NULL," +
                        "creatorName                        VARCHAR(100) NOT NULL," +
                        "UNIQUE(mediaId,creatorId)," +
                        "FOREIGN KEY(mediaId)       REFERENCES media(mediaId) ON DELETE CASCADE)");
    }

    @Override
    public Optional<TVCreators> getTvCreatorByMediaId(int mediaId) {
        return jdbcTemplate.query("SELECT * FROM creators WHERE mediaId = ?",new Object[]{mediaId},TV_CREATORS_ROW_MAPPER).stream().findFirst();
    }
}
