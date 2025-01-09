package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Cast.Actor;
import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.Media.Movie;
import ar.edu.itba.paw.services.ActorService;
import ar.edu.itba.paw.services.MediaService;
import ar.edu.itba.paw.webapp.dto.out.ActorDto;
import ar.edu.itba.paw.webapp.dto.out.MediaDto;
import ar.edu.itba.paw.webapp.dto.out.MovieDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("cast")
@Component
public class CastController {

    private final ActorService actorService;
    private final MediaService mediaService;

    @Context
    UriInfo uriInfo;

    @Autowired
    public CastController(ActorService actorService, MediaService mediaService) {
        this.actorService = actorService;
        this.mediaService = mediaService;
    }

    @GET
    @Path("actor/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActor(@PathParam("id") final int id) {
        return Response.ok(ActorDto.fromActor(actorService.getActorById(id), uriInfo)).build();
    }

    @GET
    @Path("actor/{id}/medias")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMediaForActor(@PathParam("id") final int id) {
        List<Media> mediaList = actorService.getMediaForActor(id);

        if (mediaList == null || mediaList.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No media found for actor with ID: " + id)
                    .build();
        }

        List<MediaDto> mediaDtos = MediaDto.fromMediaList(mediaList, uriInfo);
        return Response.ok(new GenericEntity<List<MediaDto>>(mediaDtos) {}).build();
    }

    @GET
    @Path("/actor")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActorsForQuery(@QueryParam("search") final String search){
        List<Actor> actorList = actorService.getActorsForQuery(search);
        List<ActorDto> actorDtoList = ActorDto.fromActorList(actorList,uriInfo);
        return Response.ok(new GenericEntity<List<ActorDto>>(actorDtoList){}).build();
    }

    @GET
    @Path("/director/{id}/medias")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMediasForDirector(@PathParam("id") final int id) {
        List<Movie> mediaList = mediaService.getMediaForDirectorId(id);

        if (mediaList == null || mediaList.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No media found for director with ID: " + id)
                    .build();
        }

        List<MovieDto> mediaDtos = MovieDto.fromMovieList(mediaList, uriInfo);
        return Response.ok(new GenericEntity<List<MovieDto>>(mediaDtos) {}).build();
    }
}

