package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Cast.Actor;
import ar.edu.itba.paw.services.ActorService;
import ar.edu.itba.paw.webapp.dto.out.ActorDto;
import ar.edu.itba.paw.webapp.vndTypes.VndType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

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

    @GET
    @Produces(VndType.APPLICATION_ACTOR_LIST)
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

    @GET
    @Path("/{id}")
    @Produces(VndType.APPLICATION_ACTOR)
    public Response getActor(@PathParam("id") final int id) {
        return Response.ok(ActorDto.fromActor(actorService.getActorById(id), uriInfo)).build();
    }

}

