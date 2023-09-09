--Movie
CREATE TABLE movies IF NOT EXISTS
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
    voteRating              INTEGER NOT NULL,
    voteCount               INTEGER NOT NULL,
    status                  VARCHAR(20) NOT NULL,
    PRIMARY KEY(movieId)
);

CREATE TABLE movieGenres IF NOT EXISTS
(
    movieId                 INTEGER NOT NULL,
    genre                   VARCHAR(64) NOT NULL,
    FOREIGN KEY(movieId)    REFERENCES movies(movieId) ON DELETE CASCADE
);

CREATE TABLE movieCast IF NOT EXISTS
(
    movieId                 INTEGER NOT NULL,
    actorName               VARCHAR(100) NOT NULL,
    characterName           VARCHAR(100),
    profilePath             VARCHAR(255),
    FOREIGN KEY(movieId)    REFERENCES movies(movieId) ON DELETE CASCADE
);






--TV
CREATE TABLE tv IF NOT EXISTS
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
    voteRating              INTEGER NOT NULL,
    voteCount               INTEGER NOT NULL,
    status                  VARCHAR(20) NOT NULL,
    numberOfEpisodes        INTEGER NOT NULL,
    numberOfSeasons         INTEGER NOT NULL,
    PRIMARY KEY(tvId)
    );

CREATE TABLE tvGenres IF NOT EXISTS
(
    tvId                    INTEGER NOT NULL,
    genre                   VARCHAR(64) NOT NULL,
    FOREIGN KEY(tvId)       REFERENCES tv(tvId) ON DELETE CASCADE
    );

CREATE TABLE tvCast IF NOT EXISTS
(
    tvId                    INTEGER NOT NULL,
    actorName               VARCHAR(100) NOT NULL,
    characterName           VARCHAR(100),
    profilePath             VARCHAR(255),
    FOREIGN KEY(tvId)       REFERENCES tv(tvId) ON DELETE CASCADE
);

CREATE TABLE creators IF NOT EXISTS
(
    tvId                    INTEGER NOT NULL,
    creatorName             VARCHAR(100) NOT NULL,
    posterPath              VARCHAR(255),
    FOREIGN KEY(tvId)       REFERENCES tv(tvId) ON DELETE CASCADE
)

