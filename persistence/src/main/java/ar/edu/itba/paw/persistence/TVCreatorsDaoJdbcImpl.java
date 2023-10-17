package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.TV.TVCreators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class TVCreatorsDaoJdbcImpl implements TVCreatorsDao{
    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<TVCreators> TV_CREATORS_ROW_MAPPER = (rs, rowNum) -> new TVCreators(
            rs.getInt("mediaId"),
            rs.getInt("creatorId"),
            rs.getString("creatorName")
    );

    @Autowired
    public TVCreatorsDaoJdbcImpl(final DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<TVCreators> getTvCreatorsByMediaId(int mediaId) {
        return jdbcTemplate.query("SELECT * FROM creators WHERE mediaId = ?",new Object[]{mediaId},TV_CREATORS_ROW_MAPPER);
    }
}
