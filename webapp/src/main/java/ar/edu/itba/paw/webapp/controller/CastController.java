package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Cast.Actor;
import ar.edu.itba.paw.models.TV.TVCreators;
import ar.edu.itba.paw.services.ActorService;
import ar.edu.itba.paw.services.TVCreatorsService;
import ar.edu.itba.paw.webapp.dto.out.ActorDto;
import ar.edu.itba.paw.webapp.dto.out.TvCreatorsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("cast")
@Component
public class CastController {

    private final ActorService actorService;
    private final TVCreatorsService tvCreatorsService;


    @Context
    UriInfo uriInfo;

    @Autowired
    public CastController(ActorService actorService, TVCreatorsService tvCreatorsService) {
        this.actorService = actorService;
        this.tvCreatorsService = tvCreatorsService;
    }

    @GET
    @Path("/actors/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActor(@PathParam("id") final int id) {
        return Response.ok(ActorDto.fromActor(actorService.getActorById(id), uriInfo)).build();
    }

    @GET
    @Path("/actors")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActors(
            @QueryParam("mediaId") final Integer mediaId,
            @QueryParam("search") final String search
    ) {
        if (mediaId != null) {
            // Buscar actores por mediaId
            final List<ActorDto> actorDtoList = ActorDto.fromActorList(actorService.getAllActorsForMedia(mediaId), uriInfo);
            return Response.ok(new GenericEntity<List<ActorDto>>(actorDtoList) {}).build();
        } else if (search != null && !search.isEmpty()) {
            // Buscar actores por consulta de texto
            List<Actor> actorList = actorService.getActorsForQuery(search);
            List<ActorDto> actorDtoList = ActorDto.fromActorList(actorList, uriInfo);
            return Response.ok(new GenericEntity<List<ActorDto>>(actorDtoList) {}).build();
        } else {
            // Si no se proporcionan parámetros válidos
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("You must provide either 'mediaId' or 'search' as query parameters.")
                    .build();
        }
    }


    /* TVCREATORS */

    @GET
    @Path("/tvCreators/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTvCreatorById(@PathParam("id") final int tvCreatorId) {
        TVCreators tvCreators=tvCreatorsService.getTvCreatorById(tvCreatorId);
        return Response.ok(TvCreatorsDto.fromTvCreator(tvCreators,uriInfo)).build();
    }


    @GET
    @Path("/tvCreators")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTVCreators(
            @QueryParam("search") final String search,
            @QueryParam("mediaId") final Integer mediaId
    ) {
        if (search != null && !search.isEmpty()) {
            // Lógica para obtener creadores de TV por consulta de búsqueda
            List<TVCreators> tvCreatorsList = tvCreatorsService.getTVCreatorsForQuery(search, 10);
            List<TvCreatorsDto> tvCreatorsDtoList = TvCreatorsDto.fromTvCreatorList(tvCreatorsList, uriInfo);
            return Response.ok(new GenericEntity<List<TvCreatorsDto>>(tvCreatorsDtoList) {}).build();
        } else if (mediaId != null) {
            // Lógica para obtener creadores de TV por ID de medio
            List<TVCreators> tvCreators = tvCreatorsService.getTvCreatorsByMediaId(mediaId);
            List<TvCreatorsDto> tvCreatorsDtos = TvCreatorsDto.fromTvCreatorList(tvCreators, uriInfo);
            return Response.ok(new GenericEntity<List<TvCreatorsDto>>(tvCreatorsDtos) {}).build();
        }

        // Si no se proporcionan parámetros válidos
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("You must provide either 'search' or 'mediaId' as query parameters.")
                .build();
    }

}

