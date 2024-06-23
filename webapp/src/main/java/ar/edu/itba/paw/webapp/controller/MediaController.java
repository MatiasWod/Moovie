package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.Media.MediaFilters;
import ar.edu.itba.paw.models.Media.MediaTypes;
import ar.edu.itba.paw.models.PagingSizes;
import ar.edu.itba.paw.services.MediaService;
import ar.edu.itba.paw.webapp.dto.MediaDTO;
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

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listAll(@QueryParam("page") @DefaultValue("1") final int page) {
        try {
            List<Media> mediaList = mediaService.getMedia(MediaTypes.TYPE_ALL.getType(), null, null,
                    null, null, null, null, MediaFilters.TMDBRATING.getFilter(), MediaFilters.DESC.getFilter(), PagingSizes.MEDIA_DEFAULT_PAGE_SIZE.getSize(), page - 1);

            List<MediaDTO> mediaDTOList = MediaDTO.fromMediaList(mediaList, uriInfo);
            return Response.ok(new GenericEntity<List<MediaDTO>>(mediaDTOList) {
            }).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
}
