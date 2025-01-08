package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.TV.TVCreators;
import ar.edu.itba.paw.services.TVCreatorsService;
import ar.edu.itba.paw.webapp.dto.out.TvCreatorsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("tvcreators")
@Component
public class TvCreatosController {

    private final TVCreatorsService tvCreatorsService;

    @Context
    UriInfo uriInfo;

    @Autowired
    public TvCreatosController(final TVCreatorsService tvCreatorsService) {
        this.tvCreatorsService = tvCreatorsService;
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTvCreatorById(@PathParam("id") final int id) {
        TVCreators tvCreators=tvCreatorsService.getTvCreatorById(id);
        return Response.ok(TvCreatorsDto.fromTvCreator(tvCreators,uriInfo)).build();
    }


}
