package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.ResourceNotFoundException;
import ar.edu.itba.paw.models.Cast.Director;
import ar.edu.itba.paw.models.PagingSizes;
import ar.edu.itba.paw.models.PagingUtils;
import ar.edu.itba.paw.services.DirectorService;
import ar.edu.itba.paw.webapp.dto.out.DirectorDto;
import ar.edu.itba.paw.webapp.utils.ResponseUtils;
import ar.edu.itba.paw.webapp.vndTypes.VndType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.NoResultException;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("directors")
@Component
public class DirectorController {

    private final DirectorService directorService;

    @Context
    UriInfo uriInfo;

    @Autowired
    public DirectorController(DirectorService directorService) {
        this.directorService = directorService;
    }

    Logger logger = LoggerFactory.getLogger(DirectorController.class);

    @GET
    @Produces(VndType.APPLICATION_DIRECTOR_LIST)
    public Response getDirectors(
            @QueryParam("search") final String search,
            @QueryParam("pageNumber") @DefaultValue("1") final int pageNumber,
            @QueryParam("pageSize") @DefaultValue("-1") final int pageSize
    ) {
        try {
            // Determine page size
            int pageSizeQuery = pageSize;
            if (pageSize < 1 || pageSize > PagingSizes.DIRECTOR_DEFAULT_PAGE_SIZE.getSize()) {
                pageSizeQuery = PagingSizes.DIRECTOR_DEFAULT_PAGE_SIZE.getSize();
            }

            if (search != null && !search.isEmpty()) {
                // Lógica para obtener directores por consulta de búsqueda
                List<Director> directors = directorService.getDirectorsForQuery(search, pageNumber, pageSizeQuery);
                int totalCount = directorService.getDirectorsForQueryCount(search);
                List<DirectorDto> directorDtos = DirectorDto.fromDirectorList(directors, uriInfo);
                
                Response.ResponseBuilder res = Response.ok(new GenericEntity<List<DirectorDto>>(directorDtos) {});
                final PagingUtils<DirectorDto> pagingUtils = new PagingUtils<>(directorDtos, pageNumber, pageSizeQuery, totalCount);
                ResponseUtils.setPaginationLinks(res, pagingUtils, uriInfo);
                return res.build();
            }

            // Si no se proporcionan parámetros válidos
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("You must provide 'search' as query parameter.")
                    .build();
        }catch (Exception e) {
            logger.error("Error getting directors", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An error occurred while processing the request.")
                    .build();
        }

    }

    @GET
    @Path("/{id}")
    @Produces(VndType.APPLICATION_DIRECTOR)
    public Response getDirectorById(@PathParam("id") @NotNull final int directorId) {
        try {
            Director director = directorService.getDirectorById(directorId);
            Response.ResponseBuilder res = Response.ok(DirectorDto.fromDirector(director, uriInfo));
            ResponseUtils.setMaxAgeCache(res);
            return res.build();
        }catch (NoResultException e){
            throw new ResourceNotFoundException("Director not found");
        }
        catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An error occurred while processing the request.")
                    .build();
        }

    }
}
