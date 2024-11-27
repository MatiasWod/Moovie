package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.services.ActorService;
import ar.edu.itba.paw.webapp.dto.out.ActorDto;
import ar.edu.itba.paw.webapp.dto.out.MediaDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("cast")
@Component
public class CastController {

    private final ActorService actorService;

    @Context
    UriInfo uriInfo;

    @Autowired
    public CastController(ActorService actorService) {
        this.actorService = actorService;
    }

    @GET
    @Path("actor/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActor(@PathParam("id") final int id) {
        try{
            return Response.ok(ActorDto.fromActor(actorService.getActorById(id), uriInfo)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("actor/{id}/medias")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMediaForActor(@PathParam("id") final int id) {
        try {
            List<Media> mediaList = actorService.getMediaForActor(id);

            if (mediaList == null || mediaList.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("No media found for actor with ID: " + id)
                        .build();
            }

            List<MediaDto> mediaDtos = MediaDto.fromMediaList(mediaList, uriInfo);
            return Response.ok(new GenericEntity<List<MediaDto>>(mediaDtos) {}).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid actor ID: " + id)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An unexpected error occurred: " + e.getMessage())
                    .build();
        }
    }

}

