package ar.edu.itba.paw.models.Genre;

public abstract class Genre {
    private final String[] genres;

    public Genre(String[] genres){
        this.genres = genres;
    }
    public String[] getGenres(){
        return genres;
    }
}
