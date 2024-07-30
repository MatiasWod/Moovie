package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.PagingSizes;
import ar.edu.itba.paw.services.MoovieListService;
import ar.edu.itba.paw.webapp.dto.MediaDto;
import ar.edu.itba.paw.webapp.dto.MoovieListDto;
import ar.edu.itba.paw.webapp.dto.UserDto;
import org.hibernate.annotations.GeneratorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("list")
@Component
public class MoovieListController {

    private final MoovieListService moovieListService;

    private final int PAGE_SIZE = PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CONTENT.getSize();

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
            return Response.ok(MoovieListDto.fromMoovieList(moovieListService.getMoovieListCardById(id), uriInfo)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    //We have a separate endpoint for content to be able to use filters and no need to do it every time we want to find a list
    @GET
    @Path("/{id}/media")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMoovieListMedia(@PathParam("id") final int id,
                                       @QueryParam("orderBy") @DefaultValue("customOrder") final String orderBy,
                                       @QueryParam("sortOrder") @DefaultValue("DESC") final String sortOrder,
                                       @QueryParam("pageNumber") @DefaultValue("1") final int pageNumber){
        try{
            List<Media> mediaList = moovieListService.getMoovieListContent(id, orderBy, sortOrder, PAGE_SIZE, pageNumber);
            List<MediaDto> mediaDtoList = MediaDto.fromMediaList(mediaList, uriInfo);
            return Response.ok(new GenericEntity<List<MediaDto>>(mediaDtoList) {
            }).build();
        } catch(RuntimeException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
