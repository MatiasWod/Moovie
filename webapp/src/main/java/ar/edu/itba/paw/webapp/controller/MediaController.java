package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.Media.MediaFilters;
import ar.edu.itba.paw.models.Media.MediaTypes;
import ar.edu.itba.paw.models.PagingSizes;
import ar.edu.itba.paw.services.MediaService;
import ar.edu.itba.paw.webapp.dto.MediaDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("media")
@Component
public class MediaController {

    private final MediaService mediaService;

    @Context
    UriInfo uriInfo;

    @Autowired
    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    //TODO capaz considerar en listAll poder pedir paginas de distintos tamaños, tambien filtros y ordenado, hasta se podria devolder el count en esta misma query....

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMedia(@QueryParam("type") @DefaultValue("-1") final int type,
                             @QueryParam("pageNumber") @DefaultValue("1") final int page,
                             @QueryParam("pageSize") @DefaultValue("-1") final int pageSize,
                             @QueryParam("orderBy") final String orderBy,
                             @QueryParam("sortOrder") final String sortOrder) {
        /* int type, String search, String participant, List<String> genres, List<String> providers,
                List<String> status, List<String> lang, String orderBy, String sortOrder, int size, int pageNumber*/
        try {
            int typeQuery = MediaTypes.TYPE_ALL.getType();
            if(type==MediaTypes.TYPE_MOVIE.getType() || type==MediaTypes.TYPE_TVSERIE.getType()){
                typeQuery = type;
            }

            int pageSizeQuery = pageSize;
            if(pageSize<1 || pageSize>PagingSizes.MEDIA_DEFAULT_PAGE_SIZE.getSize()){
                pageSizeQuery = PagingSizes.MEDIA_DEFAULT_PAGE_SIZE.getSize();
            }

            List<Media> mediaList = mediaService.getMedia(typeQuery, null, null,
                    null, null, null, null, orderBy, sortOrder, pageSizeQuery, page - 1);

            List<MediaDto> mediaDtoList = MediaDto.fromMediaList(mediaList, uriInfo);
            return Response.ok(new GenericEntity<List<MediaDto>>(mediaDtoList) {
            }).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMediaById(@PathParam("id") final int id) {
        try{
            Media media = mediaService.getMediaById(id);
            return Response.ok(MediaDto.fromMedia(media, uriInfo)).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }



}
