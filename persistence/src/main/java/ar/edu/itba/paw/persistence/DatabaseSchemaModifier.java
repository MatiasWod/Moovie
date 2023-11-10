package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Genre.Genre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Primary
@Repository
public class DatabaseSchemaModifier implements DatabaseSchemaModifierDao{
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert MediaProvidersJdbcInsert;
    private final SimpleJdbcInsert GenresJdbcInsert;
    private final SimpleJdbcInsert MediaGenresJdbcInsert;
    private final SimpleJdbcInsert MediaCreatorsJdbcInsert;
    private final SimpleJdbcInsert MediaActorsJdbcInsert;



    private static final RowMapper<OldGenre> OLD_GENRE_ROW_MAPPER = (rs, rowNum) -> new OldGenre(
            rs.getInt("mediaid"),
            rs.getString("genre")
    );

    private static final RowMapper<OldProviders> OLD_PROVIDERS_ROW_MAPPER = (rs, rowNum) -> new OldProviders(
            rs.getInt("mediaid"),
            rs.getInt("providerid"),
            rs.getString("providername"),
            rs.getString("logopath")
    );

    private static final RowMapper<OldActors> OLD_ACTORS_ROW_MAPPER = (rs, rowNum) -> new OldActors(
            rs.getInt("mediaid"),
            rs.getInt("actorid"),
            rs.getString("actorname"),
            rs.getString("charactername"),
            rs.getString("profilepath")
    );

    private static final RowMapper<OldCreators> OLD_CREATORS_ROW_MAPPER = (rs, rowNum) -> new OldCreators(
            rs.getInt("mediaid"),
            rs.getInt("creatorid"),
            rs.getString("creatorname")
    );

    private static final RowMapper<String> GENRE_STRING_ROW_MAPPER = (rs, rowNum) -> new String(
            rs.getString("genre")
    );

    private static class OldGenre{
        int mediaid;
        String genre;

        public OldGenre(int mediaid, String genre) {
            this.mediaid = mediaid;
            this.genre = genre;
        }

        public int getMediaid() {
            return mediaid;
        }

        public String getGenre() {
            return genre;
        }

        public void setMediaid(int mediaid) {
            this.mediaid = mediaid;
        }

        public void setGenre(String genre) {
            this.genre = genre;
        }
    }

    private static class OldProviders{
        int mediaid;
        int providerid;
        String providerName;
        String logopath;

        public OldProviders(int mediaid, int providerid, String providerName, String logopath) {
            this.mediaid = mediaid;
            this.providerid = providerid;
            this.providerName = providerName;
            this.logopath = logopath;
        }

        public void setMediaid(int mediaid) {
            this.mediaid = mediaid;
        }

        public void setProviderid(int providerid) {
            this.providerid = providerid;
        }

        public void setProviderName(String providerName) {
            this.providerName = providerName;
        }

        public void setLogopath(String logopath) {
            this.logopath = logopath;
        }

        public int getMediaid() {
            return mediaid;
        }

        public int getProviderid() {
            return providerid;
        }

        public String getProviderName() {
            return providerName;
        }

        public String getLogopath() {
            return logopath;
        }
    }

    private static class OldActors{
        int mediaid;
        int actorId;
        String actorName;
        String characterName;
        String profilePath;

        public OldActors(int mediaid, int actorId, String actorName, String characterName, String profilePath) {
            this.mediaid = mediaid;
            this.actorId = actorId;
            this.actorName = actorName;
            this.characterName = characterName;
            this.profilePath = profilePath;
        }

        public void setMediaid(int mediaid) {
            this.mediaid = mediaid;
        }

        public void setActorId(int actorId) {
            this.actorId = actorId;
        }

        public void setActorName(String actorName) {
            this.actorName = actorName;
        }

        public void setCharacterName(String characterName) {
            this.characterName = characterName;
        }

        public void setProfilePath(String profilePath) {
            this.profilePath = profilePath;
        }

        public int getMediaid() {
            return mediaid;
        }

        public int getActorId() {
            return actorId;
        }

        public String getActorName() {
            return actorName;
        }

        public String getCharacterName() {
            return characterName;
        }

        public String getProfilePath() {
            return profilePath;
        }
    }

    private static class OldCreators{
        int mediaid;
        int creatorId;
        String creatorName;

        public OldCreators(int mediaid, int creatorId, String creatorName) {
            this.mediaid = mediaid;
            this.creatorId = creatorId;
            this.creatorName = creatorName;
        }

        public void setMediaid(int mediaid) {
            this.mediaid = mediaid;
        }

        public void setCreatorId(int creatorId) {
            this.creatorId = creatorId;
        }

        public void setCreatorName(String creatorName) {
            this.creatorName = creatorName;
        }

        public int getMediaid() {
            return mediaid;
        }

        public int getCreatorId() {
            return creatorId;
        }

        public String getCreatorName() {
            return creatorName;
        }
    }


    @Autowired
    public DatabaseSchemaModifier(final DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
        MediaProvidersJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("mediaproviders");
        MediaCreatorsJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("mediacreators");
        MediaActorsJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("mediactors");
        MediaGenresJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("mediagenres");
        GenresJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("genres");
    }

    @Override
    public void updateProviders(){
        jdbcTemplate.execute("DELETE FROM providers WHERE providerName = 'ABC';");

        List<OldProviders> allOldProviders =  jdbcTemplate.query("SELECT * FROM providers ",OLD_PROVIDERS_ROW_MAPPER);

        jdbcTemplate.execute("DELETE FROM providers " +
                " WHERE ctid NOT IN ( " +
                "  SELECT min(ctid) " +
                "  FROM providers " +
                "  GROUP BY providerId, providerName, logoPath " +
                ");");
        jdbcTemplate.execute("ALTER TABLE providers DROP CONSTRAINT providers_mediaid_fkey;");
        jdbcTemplate.execute("ALTER TABLE providers DROP CONSTRAINT providers_mediaid_providerid_key;");
        jdbcTemplate.execute("ALTER TABLE providers DROP COLUMN mediaid;");
        jdbcTemplate.execute("ALTER TABLE providers ADD PRIMARY KEY (providerId);");

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS mediaproviders (" +
                " mediaid   INTEGER NOT NULL, " +
                " providerid INTEGER NOT NULL, " +
                " PRIMARY KEY(mediaid, providerid), " +
                " FOREIGN KEY(mediaId)       REFERENCES media(mediaId) ON DELETE CASCADE, " +
                " FOREIGN KEY(providerid)    REFERENCES providers(providerid) ON DELETE CASCADE);"
                );

        final Map<String,Object> args = new HashMap<>();
        for(OldProviders prov : allOldProviders ){
            args.put("mediaid", prov.mediaid);
            args.put("providerid", prov.providerid);

            MediaProvidersJdbcInsert.execute(args);
        }
    }
    @Override
    public void updateActors(){
        List<OldActors> allOldActors =  jdbcTemplate.query("SELECT * FROM actors ",OLD_ACTORS_ROW_MAPPER);

        jdbcTemplate.execute("DELETE FROM actors " +
                " WHERE ctid NOT IN ( " +
                "  SELECT min(ctid) " +
                "  FROM actors " +
                "  GROUP BY actorid, actorname, profilepath " +
                ");");

        jdbcTemplate.execute("ALTER TABLE actors DROP CONSTRAINT actors_mediaid_fkey;");
        jdbcTemplate.execute("ALTER TABLE actors DROP CONSTRAINT actors_mediaid_actorid_key;");
        jdbcTemplate.execute("ALTER TABLE actors DROP COLUMN mediaid;");
        jdbcTemplate.execute("ALTER TABLE actors ADD PRIMARY KEY (actorid);");

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS mediaactors (" +
                " mediaid   INTEGER NOT NULL, " +
                " actorid INTEGER NOT NULL, " +
                " charactername VARCHAR(100), " +
                " PRIMARY KEY(mediaid, actorid), " +
                " FOREIGN KEY(mediaId)       REFERENCES media(mediaId) ON DELETE CASCADE, " +
                " FOREIGN KEY(actorid)    REFERENCES actors(actorid) ON DELETE CASCADE);"
        );

        final Map<String,Object> args = new HashMap<>();
        for(OldActors act : allOldActors ){
            args.put("mediaid", act.mediaid);
            args.put("actorid", act.actorId);
            args.put("charactername", act.characterName);

            MediaActorsJdbcInsert.execute(args);
        }
    }

    @Override
    public void updateCreators(){

        List<OldCreators> allOldCreators =  jdbcTemplate.query("SELECT * FROM creators ",OLD_CREATORS_ROW_MAPPER);

        jdbcTemplate.execute("DELETE FROM creators " +
                " WHERE ctid NOT IN ( " +
                "  SELECT min(ctid) " +
                "  FROM creators " +
                "  GROUP BY creatorId, creatorName " +
                ");");
        jdbcTemplate.execute("ALTER TABLE creators DROP CONSTRAINT creators_mediaid_fkey;");
        jdbcTemplate.execute("ALTER TABLE creators DROP CONSTRAINT creators_mediaid_creatorid_key;");
        jdbcTemplate.execute("ALTER TABLE creators DROP COLUMN mediaid;");
        jdbcTemplate.execute("ALTER TABLE creators ADD PRIMARY KEY (creatorid);");

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS mediacreators (" +
                " mediaid   INTEGER NOT NULL, " +
                " creatorid INTEGER NOT NULL, " +
                " PRIMARY KEY(mediaid, creatorid), " +
                " FOREIGN KEY(mediaId)       REFERENCES media(mediaId) ON DELETE CASCADE, " +
                " FOREIGN KEY(creatorid)    REFERENCES creators(creatorid) ON DELETE CASCADE);"
        );

        final Map<String,Object> args = new HashMap<>();
        for(OldCreators creat : allOldCreators ){
            args.put("mediaid", creat.mediaid);
            args.put("creatorid", creat.creatorId);

            MediaCreatorsJdbcInsert.execute(args);
        }
    }

    @Override
    public void updateGenres(){
        //Agarrar todos los generos
        List<String> genresString = jdbcTemplate.query("SELECT DISTINCT genres.genre FROM genres ORDER BY genres.genre",GENRE_STRING_ROW_MAPPER);
        List<OldGenre> allOldGenres = jdbcTemplate.query("SELECT * FROM genres;", OLD_GENRE_ROW_MAPPER);

        //Crear la tabla de los ids con todos los generos
        jdbcTemplate.execute("DROP TABLE genres");

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS genres( " +
                "    genreid                 INTEGER NOT NULL , " +
                "    genrename               VARCHAR(100) NOT NULL, " +
                "    PRIMARY KEY(genreid) " +
                ");");

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS mediagenres( " +
                "    mediaId                    INTEGER NOT NULL, " +
                "    genreId                    INTEGER NOT NULL, " +
                "    PRIMARY KEY(mediaId,genreid), " +
                "    FOREIGN KEY(mediaId)       REFERENCES media(mediaId) ON DELETE CASCADE,  " +
                "    FOREIGN KEY(genreid)       REFERENCES genres(genreId) ON DELETE CASCADE " +
                ");");

        int i = 1;
        final Map<String,Object> args1 = new HashMap<>();
        for( String gs : genresString ){
            args1.put("genreid", i);
            args1.put("genrename", gs);
            GenresJdbcInsert.execute(args1);
            i++;
        }

        final Map<String,Object> args2 = new HashMap<>();
        for( OldGenre og : allOldGenres ){
            args2.put("mediaid", og.mediaid);
            args2.put("genreid", genresString.indexOf(og.genre)+1);
            MediaGenresJdbcInsert.execute(args2);
        }

    }


}

