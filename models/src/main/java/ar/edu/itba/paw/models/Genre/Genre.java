package ar.edu.itba.paw.models.Genre;

public class Genre {
    private final int mediaId;
    private final String genre;

    public Genre(int mediaId, String genre) {
        this.mediaId = mediaId;
        this.genre = genre;
    }

    public int getMediaId() {
        return mediaId;
    }

    public String getGenre() {
        return genre;
    }
}
