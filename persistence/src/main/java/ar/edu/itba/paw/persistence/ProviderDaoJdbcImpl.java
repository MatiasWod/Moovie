package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Optional;

@Repository
public class ProviderDaoJdbcImpl implements ProviderDao{
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert providerJdbcInsert;

    private static final RowMapper<Provider> PROVIDER_ROW_MAPPER = (rs, rowNum) -> new Provider(
            rs.getInt("mediaId"),
            rs.getInt("providerId"),
            rs.getString("providerName"),
            rs.getString("logoPath")
    );

    private static final RowMapper<Integer> COUNT_ROW_MAPPER = ((resultSet, i) -> resultSet.getInt("count"));

    @Autowired
    public ProviderDaoJdbcImpl(final DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
        providerJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("providers").usingGeneratedKeyColumns("mediaId");
        jdbcTemplate.execute(
                "CREATE TABLE IF NOT EXISTS providers(" +
                        "mediaId                        INTEGER NOT NULL," +
                        "providerId                     INTEGER NOT NULL," +
                        "providerName                   VARCHAR(100) NOT NULL," +
                        "logoPath                       VARCHAR(100) NOT NULL," +
                        "UNIQUE(mediaId,providerId)," +
                        "FOREIGN KEY(mediaId)       REFERENCES media(mediaId) ON DELETE CASCADE)");
    }

    @Override
    public Optional<Provider> getProviderForMedia(int mediaId) {
        return jdbcTemplate.query("SELECT * FROM providers WHERE mediaId = ?",new Object[]{mediaId},PROVIDER_ROW_MAPPER).stream().findFirst();
    }
}
