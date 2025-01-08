package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.TV.TVCreators;
import ar.edu.itba.paw.services.TVCreatorsService;
import ar.edu.itba.paw.webapp.dto.out.MediaDto;
import ar.edu.itba.paw.webapp.dto.out.TvCreatorsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("tvcreators")
@Component
public class TvCreatorsController {

    private final TVCreatorsService tvCreatorsService;

    @Context
    UriInfo uriInfo;

    @Autowired
    public TvCreatorsController(final TVCreatorsService tvCreatorsService) {
        this.tvCreatorsService = tvCreatorsService;
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTvCreatorById(@PathParam("id") final int id) {
        TVCreators tvCreators=tvCreatorsService.getTvCreatorById(id);
        return Response.ok(TvCreatorsDto.fromTvCreator(tvCreators,uriInfo)).build();
    }

    @GET
    @Path("{id}/medias")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMediasForTVCreator(@PathParam("id") final int id) {
        List<Media> mediaList = tvCreatorsService.getMediasForTVCreator(id);

        if (mediaList == null || mediaList.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No media found for tv creator with ID: " + id)
                    .build();
        }

        List<MediaDto> mediaDtos = MediaDto.fromMediaList(mediaList, uriInfo);
        return Response.ok(new GenericEntity<List<MediaDto>>(mediaDtos) {}).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTVCreatorsForQuery(@QueryParam("search") final String search){
        List<TVCreators> tvCreatorsList = tvCreatorsService.getTVCreatorsForQuery(search, 10);
        List<TvCreatorsDto> tvCreatorsDtoList = TvCreatorsDto.fromTvCreatorList(tvCreatorsList,uriInfo);
        return Response.ok(new GenericEntity<List<TvCreatorsDto>>(tvCreatorsDtoList){}).build();
    }
}
