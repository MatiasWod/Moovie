package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Genre.Genre;
import ar.edu.itba.paw.services.GenreService;
import ar.edu.itba.paw.webapp.dto.out.GenreDto;
import ar.edu.itba.paw.webapp.dto.out.ResponseMessage;
import ar.edu.itba.paw.webapp.utils.ResponseUtils;
import ar.edu.itba.paw.webapp.vndTypes.VndType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
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
    @Produces(VndType.APPLICATION_GENRE_LIST)
    public Response getGenres(@QueryParam("mediaId") final Integer mediaId) {
        try {
            List<Genre> genreList;

            // Si se proporciona un mediaId, obtener géneros para ese medio
            if (mediaId != null) {
                genreList = genreService.getGenresForMedia(mediaId);
            }
            // Si no se proporciona un mediaId, obtener todos los géneros
            else {
                genreList = genreService.getAllGenres();
            }

            // Convertir la lista de géneros a DTOs
            final List<GenreDto> genreDtoList = GenreDto.fromGenreList(genreList, uriInfo);

            // Devolver la respuesta con la lista de DTOs
            Response.ResponseBuilder res = Response.ok(new GenericEntity<List<GenreDto>>(genreDtoList) {});
            ResponseUtils.setMaxAgeCache(res);
            return res.build();
        } catch (RuntimeException e) {
            // Manejar errores
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @GET()
    @Path("/{genreId}")
    @Produces(VndType.APPLICATION_GENRE)
    public Response getGenre(@PathParam("genreId") final Integer genreId) {
        Genre genre = genreService.getGenreById(genreId);
        Response.ResponseBuilder res = Response.ok(GenreDto.fromGenre(genre, uriInfo));
        ResponseUtils.setMaxAgeCache(res);
        return res.build();
    }
}
