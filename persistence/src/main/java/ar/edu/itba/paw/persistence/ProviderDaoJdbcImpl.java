package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Provider.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ProviderDaoJdbcImpl implements ProviderDao{
    private final JdbcTemplate jdbcTemplate;

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
    }

    @Override
    public List<Provider> getProviderForMedia(int mediaId) {
        return jdbcTemplate.query("SELECT * FROM providers WHERE mediaId = ?",new Object[]{mediaId},PROVIDER_ROW_MAPPER);
    }
}
