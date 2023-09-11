--Movie
CREATE TABLE IF NOT EXISTS movies
(
    movieId                 INTEGER NOT NULL,
    movieName               VARCHAR(255) NOT NULL,
    releaseDate             DATE NOT NULL,
    runtime                 INTEGER NOT NULL,
    originalLanguage        VARCHAR(2),
    adult                   BOOLEAN NOT NULL,
    overview                TEXT NOT NULL,
    backdropPath            VARCHAR(255),
    posterPath              VARCHAR(255),
    trailerLink             VARCHAR(255),
    budget                  INTEGER,
    revenue                 INTEGER,
    totalRating             INTEGER NOT NULL,
    voteCount               INTEGER NOT NULL,
    status                  VARCHAR(20) NOT NULL,
    PRIMARY KEY(movieId)
);

CREATE TABLE IF NOT EXISTS movieGenres
(
    movieId                 INTEGER NOT NULL,
    genre                   VARCHAR(64) NOT NULL,
    FOREIGN KEY(movieId)    REFERENCES movies(movieId) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS movieCast
(
    movieId                 INTEGER NOT NULL,
    actorName               VARCHAR(100) NOT NULL,
    characterName           VARCHAR(100),
    profilePath             VARCHAR(255),
    FOREIGN KEY(movieId)    REFERENCES movies(movieId) ON DELETE CASCADE
);






--TV
CREATE TABLE IF NOT EXISTS tv
(
    tvId                    INTEGER NOT NULL,
    tvName                  VARCHAR(255) NOT NULL,
    releaseDate             DATE NOT NULL,
    lastAirDate             DATE,
    nextEpisodeToAir        DATE,
    originalLanguage        VARCHAR(2),
    adult                   BOOLEAN NOT NULL,
    overview                TEXT NOT NULL,
    backdropPath            VARCHAR(255),
    posterPath              VARCHAR(255),
    trailerLink             VARCHAR(255),
    totalRating              INTEGER NOT NULL,
    voteCount               INTEGER NOT NULL,
    status                  VARCHAR(20) NOT NULL,
    numberOfEpisodes        INTEGER NOT NULL,
    numberOfSeasons         INTEGER NOT NULL,
    PRIMARY KEY(tvId)
    );

CREATE TABLE IF NOT EXISTS tvGenres
(
    tvId                    INTEGER NOT NULL,
    genre                   VARCHAR(64) NOT NULL,
    FOREIGN KEY(tvId)       REFERENCES tv(tvId) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS tvCast
(
    tvId                    INTEGER NOT NULL,
    actorName               VARCHAR(100) NOT NULL,
    characterName           VARCHAR(100),
    profilePath             VARCHAR(255),
    FOREIGN KEY(tvId)       REFERENCES tv(tvId) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS creators
(
    tvId                    INTEGER NOT NULL,
    creatorName             VARCHAR(100) NOT NULL,
    profilePath              VARCHAR(255),
    FOREIGN KEY(tvId)       REFERENCES tv(tvId) ON DELETE CASCADE
)

