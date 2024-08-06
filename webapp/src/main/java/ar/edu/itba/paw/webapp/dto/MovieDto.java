package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.Media.Movie;

import javax.persistence.Column;
import javax.ws.rs.core.UriInfo;

public class MovieDto extends MediaDto{
    private Integer runtime;

    private Long budget;

    private Long revenue;

    private Integer directorId;

    private String director;

    public static MovieDto fromMovie(Movie movie, UriInfo uri){
        MovieDto movieDto = new MovieDto();
        MediaDto.setFromMediaChild(movieDto, movie, uri);
        movieDto.budget = movie.getBudget();
        movieDto.revenue = movie.getRevenue();
        movieDto.directorId = movie.getDirectorId();
        movieDto.director = movie.getDirector();

        return movieDto;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    public Long getBudget() {
        return budget;
    }

    public void setBudget(Long budget) {
        this.budget = budget;
    }

    public Long getRevenue() {
        return revenue;
    }

    public void setRevenue(Long revenue) {
        this.revenue = revenue;
    }

    public Integer getDirectorId() {
        return directorId;
    }

    public void setDirectorId(Integer directorId) {
        this.directorId = directorId;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }
}
