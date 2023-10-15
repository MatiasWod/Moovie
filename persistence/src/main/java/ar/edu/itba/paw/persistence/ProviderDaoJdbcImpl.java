package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Provider.Provider;
import ar.edu.itba.paw.models.Provider.dimensionedProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProviderDaoJdbcImpl implements ProviderDao{
    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<Provider> PROVIDER_ROW_MAPPER = (rs, rowNum) -> new Provider(
            rs.getInt("providerId"),
            rs.getString("providerName"),
            rs.getString("logoPath")
    );
    private static final RowMapper<dimensionedProvider> DIMENSIONED_PROVIDER_ROW_MAPPER = (rs, rowNum) -> new dimensionedProvider(
            rs.getInt("providerId"),
            rs.getString("providerName"),
            rs.getString("logoPath"),
            rs.getInt("appearanceCount")
    );

    private static final RowMapper<Integer> COUNT_ROW_MAPPER = ((resultSet, i) -> resultSet.getInt("count"));

    @Autowired
    public ProviderDaoJdbcImpl(final DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<dimensionedProvider> getAllProviders() {
        StringBuilder sql = new StringBuilder("SELECT p.providerId, p.providerName, p.logoPath, COUNT(*) as appearanceCount" +
                " FROM providers p GROUP BY p.providerId, p.providerName, p.logoPath" +
                " ORDER BY appearanceCount DESC");
        ArrayList<Object> args = new ArrayList<>();
        return jdbcTemplate.query(sql.toString(),args.toArray(),DIMENSIONED_PROVIDER_ROW_MAPPER);
    }


}
