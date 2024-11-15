package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.services.ActorService;
import ar.edu.itba.paw.webapp.dto.ActorDto;
import ar.edu.itba.paw.webapp.dto.MoovieListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

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

}

