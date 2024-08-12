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

    @Context
    UriInfo uriInfo;

    @Autowired
    public MoovieListController(MoovieListService moovieListService) {
        this.moovieListService = moovieListService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMoovieList(@QueryParam("search") @DefaultValue("") String search,
                                    @QueryParam("ownerUsername") @DefaultValue("") String ownerUsername,
                                    @QueryParam("type") int type,
                                    @QueryParam("orderBy") String orderBy,
                                    @QueryParam("order") String order,
                                    @QueryParam("pageNumber") @DefaultValue("1") final int pageNumber,
                                    @QueryParam("pageSize") @DefaultValue("-1") final int pageSize){
        try{
            List<MoovieListDto> mlcDto = MoovieListDto.fromMoovieListList(moovieListService.getMoovieListCards(search, ownerUsername, type, orderBy, order, pageSize, pageNumber), uriInfo);
            return Response.ok(new GenericEntity<List<MoovieListDto>>(mlcDto){}).build();
        }catch (RuntimeException e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
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
    @Path("/{id}/content")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMoovieListMedia(@PathParam("id") final int id,
                                       @QueryParam("orderBy") @DefaultValue("customOrder") final String orderBy,
                                       @QueryParam("sortOrder") @DefaultValue("DESC") final String sortOrder,
                                       @QueryParam("pageNumber") @DefaultValue("1") final int pageNumber,
                                       @QueryParam("pageSize") @DefaultValue("-1") final int pageSize){
        try{
            int pageSizeQuery = pageSize;
            if( pageSize < 1 || pageSize > PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CONTENT.getSize() ){
                pageSizeQuery = PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CONTENT.getSize();
            }

            List<Media> mediaList = moovieListService.getMoovieListContent(id, orderBy, sortOrder, pageSizeQuery, pageNumber);
            List<MediaDto> mediaDtoList = MediaDto.fromMediaList(mediaList, uriInfo);
            return Response.ok(new GenericEntity<List<MediaDto>>(mediaDtoList) {
            }).build();
        } catch(RuntimeException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

}
