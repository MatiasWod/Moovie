package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Genre.Genre;
import ar.edu.itba.paw.services.GenreService;
import ar.edu.itba.paw.webapp.dto.out.GenreDto;
import ar.edu.itba.paw.webapp.dto.out.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;
import java.util.List;

@Path("genres")
@Component
public class GenreController {
    @Autowired
    private GenreService genreService;

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllGenres(){
            final List<Genre> genreList = genreService.getAllGenres();
            final List<GenreDto> genreDtoList = GenreDto.fromGenreList(genreList,uriInfo);
            return Response.ok(new GenericEntity<List<GenreDto>>(genreDtoList){}).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGenresForMedia(@PathParam("id") final int mediaId){
            final List<Genre> genreList = genreService.getGenresForMedia(mediaId);
            final List<GenreDto> genreDtoList = GenreDto.fromGenreList(genreList,uriInfo);
            return Response.ok(new GenericEntity<List<GenreDto>>(genreDtoList){}).build();
    }
}
