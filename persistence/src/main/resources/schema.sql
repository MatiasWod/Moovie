--User
CREATE TABLE IF NOT EXISTS users (
    userId                          SERIAL PRIMARY KEY,
    email                           VARCHAR(255) UNIQUE NOT NULL,
    CONSTRAINT valid_email_address  CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+[A-Za-z]{2,}$')
);

--MoovieLists
CREATE TABLE IF NOT EXISTS moovieLists(
    moovieListId                        SERIAL PRIMARY KEY,
    userId                              INTEGER NOT NULL,
    name                                VARCHAR(255) NOT NULL,
    description                         TEXT,
    FOREIGN KEY(userId) REFERENCES users(userId) ON DELETE CASCADE,
    UNIQUE(userId,name)
);

--Media
CREATE TABLE IF NOT EXISTS media(
    mediaId                        SERIAL PRIMARY KEY,
    type                           BOOLEAN NOT NULL,
    name                           VARCHAR(255) NOT NULL,
    originalLanguage               VARCHAR(2),
    adult                          BOOLEAN NOT NULL,
    releaseDate                    DATE NOT NULL,
    overview                       TEXT NOT NULL,
    backdropPath                   VARCHAR(255),
    posterPath                     VARCHAR(255),
    trailerLink                    VARCHAR(255),
    tmdbRating                     FLOAT NOT NULL,
    totalRating                    INTEGER NOT NULL,
    voteCount                      INTEGER NOT NULL,
    status                         VARCHAR(20) NOT NULL
);

--MoovieListsContent
CREATE TABLE IF NOT EXISTS moovieListsContent(
    moovieListId                        INTEGER NOT NULL,
    mediaId                            INTEGER NOT NULL,
    status                             VARCHAR(30),
    UNIQUE(moovieListId,mediaId),
    FOREIGN KEY(moovieListId) REFERENCES moovieLists(moovieListId) ON DELETE CASCADE,
    FOREIGN KEY(mediaId) REFERENCES media(mediaId) ON DELETE CASCADE
);

--Reviews
CREATE TABLE IF NOT EXISTS reviews(
    reviewId                                    SERIAL PRIMARY KEY,
    userId                                      INTEGER NOT NULL,
    mediaId                                     INTEGER NOT NULL,
    rating                                      INTEGER NOT NULL CHECK(rating BETWEEN 1 AND 10),
    reviewLikes                                 INTEGER NOT NULL,
    reviewContent                               TEXT,
    FOREIGN KEY(userId) REFERENCES users(userId) ON DELETE CASCADE,
    FOREIGN KEY(mediaId) REFERENCES media(mediaId) ON DELETE CASCADE,
    UNIQUE(userId,mediaId)
);

--Movies
CREATE TABLE IF NOT EXISTS movies(
    mediaId                         INTEGER NOT NULL,
    runtime                         INTEGER,
    budget                          BIGINT,
    revenue                         BIGINT,
    directorId                      INTEGER,
    director                        VARCHAR(255),
    UNIQUE(mediaId),
    FOREIGN KEY(mediaId) REFERENCES media(mediaId) ON DELETE CASCADE
);

--TV
CREATE TABLE IF NOT EXISTS tv(
    mediaId                        INTEGER NOT NULL,
    lastAirDate                    DATE,
    nextEpisodeToAir               DATE,
    numberOfEpisodes               INTEGER,
    numberOfSeasons                INTEGER,
    UNIQUE(mediaId),
    FOREIGN KEY(mediaId) REFERENCES media(mediaId) ON DELETE CASCADE
);

--Genres
CREATE TABLE IF NOT EXISTS genres(
    mediaId                    INTEGER NOT NULL,
    genre                      VARCHAR(100) NOT NULL,
    UNIQUE(mediaId,genre),
    FOREIGN KEY(mediaId)       REFERENCES media(mediaId) ON DELETE CASCADE
);

--Actors
CREATE TABLE IF NOT EXISTS actors(
    mediaId                 INTEGER NOT NULL,
    actorId                 INTEGER NOT NULL,
    actorName               VARCHAR(100) NOT NULL,
    characterName           VARCHAR(100),
    profilePath             VARCHAR(255),
    UNIQUE(mediaId,actorId),
    FOREIGN KEY(mediaId) REFERENCES media(mediaId) ON DELETE CASCADE
);

--Creators
CREATE TABLE IF NOT EXISTS creators(
    mediaId                            INTEGER NOT NULL,
    creatorId                          INTEGER NOT NULL,
    creatorName                        VARCHAR(100) NOT NULL,
    UNIQUE(mediaId,creatorId),
    FOREIGN KEY(mediaId)       REFERENCES media(mediaId) ON DELETE CASCADE
);

--Providers
CREATE TABLE IF NOT EXISTS providers(
    mediaId                        INTEGER NOT NULL,
    providerId                     INTEGER NOT NULL,
    providerName                   VARCHAR(100) NOT NULL,
    logoPath                       VARCHAR(100) NOT NULL,
    UNIQUE(mediaId,providerId),
    FOREIGN KEY(mediaId)       REFERENCES media(mediaId) ON DELETE CASCADE
);