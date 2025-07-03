package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Cast.Actor;
import ar.edu.itba.paw.models.PagingSizes;
import ar.edu.itba.paw.models.PagingUtils;
import ar.edu.itba.paw.services.ActorService;
import ar.edu.itba.paw.webapp.dto.out.ActorDto;
import ar.edu.itba.paw.webapp.utils.ResponseUtils;
import ar.edu.itba.paw.webapp.vndTypes.VndType;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.NoResultException;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.NoSuchElementException;

@Api(value = "/actors" )
@Path("actors")
@Component
public class ActorsController {

    private final ActorService actorService;

    @Context
    UriInfo uriInfo;

    @Autowired
    public ActorsController(ActorService actorService) {
        this.actorService = actorService;
    }

    Logger logger = LoggerFactory.getLogger(ActorsController.class);

    @GET
    @Produces(VndType.APPLICATION_ACTOR_LIST)
    public Response getActors(
            @QueryParam("mediaId") final Integer mediaId,
            @QueryParam("search") final String search,
            @QueryParam("pageNumber") @DefaultValue("1") final Integer pageNumber,
            @QueryParam("pageSize") @DefaultValue("-1") final int pageSize) {
        try {
            int pageSizeQuery = pageSize;
            if (pageSize < 1 || pageSize > PagingSizes.ACTOR_DEFAULT_PAGE_SIZE.getSize()) {
                pageSizeQuery = PagingSizes.ACTOR_DEFAULT_PAGE_SIZE.getSize();
            }
            if (mediaId != null) {
                logger.info("Getting actors for mediaId {}", mediaId);
                // Buscar actores por mediaId
                final List<ActorDto> actorDtoList = ActorDto.fromActorList(
                        actorService.getAllActorsForMedia(mediaId, pageNumber, pageSizeQuery),
                        uriInfo);
                int totalCount = actorService.getAllActorsForMediaCount(mediaId);
                Response.ResponseBuilder res = Response.ok(new GenericEntity<List<ActorDto>>(actorDtoList) {
                });
                ResponseUtils.setMaxAgeCache(res);
                final PagingUtils<ActorDto> actorPagingUtils = new PagingUtils<>(actorDtoList, pageNumber, pageSizeQuery,
                        totalCount);
                ResponseUtils.setPaginationLinks(res, actorPagingUtils, uriInfo);
                return res.build();

            } else if (search != null && !search.isEmpty()) {
                logger.info("Getting actors for search {}", search);
                // Buscar actores por consulta de texto
                List<Actor> actorList = actorService.getActorsForQuery(search, pageNumber, pageSizeQuery);
                int totalCount = actorService.getActorsForQueryCount(search);
                List<ActorDto> actorDtoList = ActorDto.fromActorList(actorList, uriInfo);
                Response.ResponseBuilder res = Response.ok(new GenericEntity<List<ActorDto>>(actorDtoList) {
                });
                final PagingUtils<Actor> actorPagingUtils = new PagingUtils<>(actorList, pageNumber, pageSizeQuery,
                        totalCount);
                ResponseUtils.setPaginationLinks(res, actorPagingUtils, uriInfo);
                return res.build();
            } else {
                // Si no se proporcionan parámetros válidos
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("You must provide either 'mediaId' or 'search' as query parameters.")
                        .build();
            }
        } catch (Exception e) {
            logger.error("Error getting actors", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An error occurred while processing the request.")
                    .build();
        }

    }

    @GET
    @Path("/{id}")
    @Produces(VndType.APPLICATION_ACTOR)
    public Response getActor(@PathParam("id") @NotNull final int id) {
        try {
            Actor actor = actorService.getActorById(id);
            Response.ResponseBuilder res = Response.ok(ActorDto.fromActor(actor, uriInfo));
            ResponseUtils.setMaxAgeCache(res);
            return res.build();
        } catch (NoResultException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Actor not found.")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An error occurred while processing the request.")
                    .build();
        }
    }

}
