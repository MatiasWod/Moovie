package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Genre.Genre;
import ar.edu.itba.paw.models.PagingSizes;
import ar.edu.itba.paw.models.PagingUtils;
import ar.edu.itba.paw.services.GenreService;
import ar.edu.itba.paw.webapp.dto.out.GenreDto;
import ar.edu.itba.paw.webapp.dto.out.ProviderDto;
import ar.edu.itba.paw.webapp.dto.out.ResponseMessage;
import ar.edu.itba.paw.webapp.utils.ResponseUtils;
import ar.edu.itba.paw.webapp.vndTypes.VndType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger logger = LoggerFactory.getLogger(GenreController.class);


    @GET
    @Produces(VndType.APPLICATION_GENRE_LIST)
    public Response getGenres(@QueryParam("mediaId") final Integer mediaId,
                              @QueryParam("pageNumber") @DefaultValue("1") final Integer pageNumber) {
        List<Genre> genreList;
        int totalCount;

        int pageSize = PagingSizes.GENRE_DEFAULT_PAGE_SIZE.getSize();

        // Si se proporciona un mediaId, obtener géneros para ese medio
        if (mediaId != null) {
            genreList = genreService.getGenresForMedia(mediaId, pageSize, pageNumber);
            totalCount = genreService.getGenresForMediaCount(mediaId);
        }
        // Si no se proporciona un mediaId, obtener todos los géneros
        else {
            genreList = genreService.getAllGenres(pageSize, pageNumber);
            totalCount = genreService.getAllGenresCount();
        }

        // Convertir la lista de géneros a DTOs
        final List<GenreDto> genreDtoList = GenreDto.fromGenreList(genreList, uriInfo);

        // Devolver la respuesta con la lista de DTOs
        Response.ResponseBuilder res = Response.ok(new GenericEntity<List<GenreDto>>(genreDtoList) {});
        final PagingUtils<GenreDto> genreDtoPagingUtils = new PagingUtils<>(genreDtoList, pageNumber, pageSize,
                totalCount);
        ResponseUtils.setPaginationLinks(res, genreDtoPagingUtils, uriInfo);
        ResponseUtils.setMaxAgeCache(res);
        return res.build();
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
