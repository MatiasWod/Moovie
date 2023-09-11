package ar.edu.itba.paw.models.Genre;

public abstract class Genre {
    private final String genre;

    public Genre(String genre){
        this.genre = genre;
    }
    public String getGenre(){
        return genre;
    }
}
