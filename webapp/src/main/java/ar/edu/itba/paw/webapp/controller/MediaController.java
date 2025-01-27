package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.*;
import ar.edu.itba.paw.models.Cast.Actor;
import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.Media.MediaTypes;
import ar.edu.itba.paw.models.PagingSizes;
import ar.edu.itba.paw.models.PagingUtils;
import ar.edu.itba.paw.models.Review.MoovieListReview;
import ar.edu.itba.paw.models.Review.Review;
import ar.edu.itba.paw.models.Review.ReviewTypes;
import ar.edu.itba.paw.models.TV.TVCreators;
import ar.edu.itba.paw.services.ActorService;
import ar.edu.itba.paw.services.MediaService;
import ar.edu.itba.paw.services.ReviewService;
import ar.edu.itba.paw.services.TVCreatorsService;
import ar.edu.itba.paw.webapp.dto.in.ReviewCreateDto;
import ar.edu.itba.paw.webapp.dto.out.*;
import ar.edu.itba.paw.webapp.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.ArrayList;
import java.util.List;

@Path("medias")
@Component
public class MediaController {

    private final MediaService mediaService;
    private final TVCreatorsService tvCreatorsService;

    @Context
    UriInfo uriInfo;

    @Autowired
    public MediaController(MediaService mediaService,TVCreatorsService tvCreatorsService) {
        this.mediaService = mediaService;
        this.tvCreatorsService= tvCreatorsService;
    }

    //TODO capaz considerar en listAll poder pedir paginas de distintos tamaños, tambien filtros y
    // ordenado, hasta se podria devolder el count en esta misma query....

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMediaById(@QueryParam("ids") final String ids) {
        if (ids.length() > 100) {
            throw new IllegalArgumentException("Invalid ids, param. A comma separated list of Media IDs. Up to 100 are allowed in a single request.");
        }
        List<Integer> idList = new ArrayList<>();
        if (ids != null && !ids.isEmpty()) {
            String[] splitIds = ids.split(",");
            for (String id : splitIds) {
                try {
                    idList.add(Integer.parseInt(id.trim()));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid ids, param. A comma separated list of Media IDs. Up to 100 are allowed in a single request.");
                }
            }
        }
        if(idList.size() >= 25 || idList.size() < 0 ) {
            throw new IllegalArgumentException("Invalid ids, param. A comma separated list of Media IDs. Up to 100 are allowed in a single request.");
        }
        List<MediaDto> mediaList = new ArrayList<>();
        for (int id : idList) {
            Media media = mediaService.getMediaById(id);
            if(media.isType()){
                mediaList.add(TVSerieDto.fromTVSerie(mediaService.getTvById(id), uriInfo));
            } else{
                mediaList.add(MovieDto.fromMovie(mediaService.getMovieById(id), uriInfo));
            }
        }
        return Response.ok(new GenericEntity<List<MediaDto>>(mediaList) {}).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMediaById(@PathParam("id") final int id) {
        Media media = mediaService.getMediaById(id);
        if(media.isType()){
            return Response.ok(TVSerieDto.fromTVSerie(mediaService.getTvById(id), uriInfo)).build();
        }
        return Response.ok(MovieDto.fromMovie(mediaService.getMovieById(id), uriInfo)).build();

    }

    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMedia(@QueryParam("type") @DefaultValue("-1") final int type,
                             @QueryParam("pageNumber") @DefaultValue("1") final int page,
                             @QueryParam("pageSize") @DefaultValue("-1") final int pageSize,
                             @QueryParam("orderBy") final String orderBy,
                             @QueryParam("sortOrder") final String sortOrder,
                             @QueryParam("search") final String search,
                             @QueryParam("providers") final List<Integer> providers,
                             @QueryParam("genres") final List<Integer> genres) {
        /* int type, String search, String participant, List<String> genres, List<String> providers,
                List<String> status, List<String> lang, String orderBy, String sortOrder, int size, int pageNumber*/
        int typeQuery = MediaTypes.TYPE_ALL.getType();
        if(type==MediaTypes.TYPE_MOVIE.getType() || type==MediaTypes.TYPE_TVSERIE.getType()){
            typeQuery = type;
        }

        int pageSizeQuery = pageSize;
        if(pageSize<1 || pageSize>PagingSizes.MEDIA_DEFAULT_PAGE_SIZE.getSize()){
            pageSizeQuery = PagingSizes.MEDIA_DEFAULT_PAGE_SIZE.getSize();
        }

        List<Media> mediaList = mediaService.getMedia(typeQuery, search, null,
                genres, providers, null, null, orderBy, sortOrder, pageSizeQuery, page - 1);

        final int mediaCount = mediaService.getMediaCount(typeQuery, search, null,
                genres, providers, null, null);

        List<MediaDto> mediaDtoList = MediaDto.fromMediaList(mediaList, uriInfo);
        Response.ResponseBuilder res = Response.ok(new GenericEntity<List<MediaDto>>(mediaDtoList) {
        });
        final PagingUtils<Media> toReturnMediaList = new PagingUtils<>(mediaList,page - 1, pageSizeQuery, mediaCount);
        ResponseUtils.setPaginationLinks(res,toReturnMediaList,uriInfo);
        return res.build();
    }

    /* TVCREATORS */
    @GET
    @Path("{id}/tvcreators")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTvCreatorsByMediaID(@PathParam("id") final int id){
        List<TVCreators> tvCreators=tvCreatorsService.getTvCreatorsByMediaId(id);
        List<TvCreatorsDto> tvCreatorsDtos=TvCreatorsDto.fromTvCreatorList(tvCreators,uriInfo);
        return Response.ok(new GenericEntity<List<TvCreatorsDto>>( tvCreatorsDtos ) {}).build();
    }
}
