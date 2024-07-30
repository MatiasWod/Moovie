package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.services.MoovieListService;
import ar.edu.itba.paw.webapp.dto.MediaDto;
import ar.edu.itba.paw.webapp.dto.MoovieListDto;
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

@Path("list")
@Component
public class MoovieListController {

    private final MoovieListService moovieListService;

    @Context
    UriInfo uriInfo;

    @Autowired
    public MoovieListController(MoovieListService moovieListService) {
        this.moovieListService = moovieListService;
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMoovieListById(@PathParam("id") final int id) {
        try{
            return Response.ok(MoovieListDto.fromMoovieList(moovieListService.getMoovieListById(id), uriInfo)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
