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
import ar.edu.itba.paw.services.ActorService;
import ar.edu.itba.paw.services.MediaService;
import ar.edu.itba.paw.services.ReviewService;
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
    private final ReviewService reviewService;
    private final ActorService actorService;

    @Context
    UriInfo uriInfo;

    @Autowired
    public MediaController(MediaService mediaService, ReviewService reviewService, ActorService actorService) {
        this.mediaService = mediaService;
        this.reviewService = reviewService;
        this.actorService = actorService;
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

    /* REVIEWS */
    @GET
    @Path("/{id}/reviews")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReviewsByMediaId(@PathParam("id") final int mediaId, @QueryParam("pageNumber") @DefaultValue("1") final int page) {
        final List<Review> reviews = reviewService.getReviewsByMediaId(mediaId, PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize(), page-1);
        final int reviewCount = reviewService.getReviewsByMediaIdCount(mediaId);
        final List<ReviewDto> reviewDtos = ReviewDto.fromReviewList(reviews, uriInfo);
        Response.ResponseBuilder res = Response.ok(new GenericEntity<List<ReviewDto>>(reviewDtos) {});
        final PagingUtils<Review> reviewPagingUtils = new PagingUtils<>(reviews,page,PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize(),reviewCount);
        ResponseUtils.setPaginationLinks(res,reviewPagingUtils,uriInfo);
        return res.build();
    }


    @GET
    @Path("/{id}/actors")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActorsByMediaId(@PathParam("id") final int mediaId) {
        final List<ActorDto> actorDtoList = ActorDto.fromActorList(actorService.getAllActorsForMedia(mediaId), uriInfo);
        return Response.ok(new GenericEntity<List<ActorDto>>( actorDtoList ) {}).build();
    }


    @POST
    @Path("{id}/review")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createReview(@PathParam("id") int mediaId,@Valid final ReviewCreateDto reviewDto) {
         reviewService.createReview(
                mediaId,
                reviewDto.getRating(),
                reviewDto.getReviewContent(),
                ReviewTypes.REVIEW_MEDIA
        );

         return Response.status(Response.Status.CREATED)
                 .entity("Review successfully created to the media with ID: " + mediaId)
                 .build();
    }



    @PUT
    @Path("{id}/review")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response editReview(@PathParam("id") int mediaId,@Valid final ReviewCreateDto reviewDto) {
        reviewService.editReview(
                mediaId,
                reviewDto.getRating(),
                reviewDto.getReviewContent(),
                ReviewTypes.REVIEW_MEDIA
        );

        return Response.ok()
                .entity("Review successfully updated for media with ID: " + mediaId)
                .build();
    }
}
