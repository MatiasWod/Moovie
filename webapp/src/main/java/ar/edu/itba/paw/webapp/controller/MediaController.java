package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.ReviewNotFoundException;
import ar.edu.itba.paw.exceptions.UserNotLoggedException;
import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.Media.MediaTypes;
import ar.edu.itba.paw.models.PagingSizes;
import ar.edu.itba.paw.models.PagingUtils;
import ar.edu.itba.paw.models.Review.Review;
import ar.edu.itba.paw.models.Review.ReviewTypes;
import ar.edu.itba.paw.services.MediaService;
import ar.edu.itba.paw.services.ReviewService;
import ar.edu.itba.paw.webapp.dto.in.ReviewCreateDto;
import ar.edu.itba.paw.webapp.dto.out.MediaDto;
import ar.edu.itba.paw.webapp.dto.out.MovieDto;
import ar.edu.itba.paw.webapp.dto.out.ReviewDto;
import ar.edu.itba.paw.webapp.dto.out.TVSerieDto;
import ar.edu.itba.paw.webapp.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("media")
@Component
public class MediaController {

    private final MediaService mediaService;
    private final ReviewService reviewService;

    @Context
    UriInfo uriInfo;

    @Autowired
    public MediaController(MediaService mediaService, ReviewService reviewService) {
        this.mediaService = mediaService;
        this.reviewService = reviewService;
    }

    //TODO capaz considerar en listAll poder pedir paginas de distintos tamaños, tambien filtros y ordenado, hasta se podria devolder el count en esta misma query....

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMedia(@QueryParam("type") @DefaultValue("-1") final int type,
                             @QueryParam("pageNumber") @DefaultValue("1") final int page,
                             @QueryParam("pageSize") @DefaultValue("-1") final int pageSize,
                             @QueryParam("orderBy") final String orderBy,
                             @QueryParam("sortOrder") final String sortOrder,
                             @QueryParam("search") final String search) {
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

            List<Media> mediaList = mediaService.getMedia(typeQuery, search, null,
                    null, null, null, null, orderBy, sortOrder, pageSizeQuery, page - 1);

            final int mediaCount = mediaService.getMediaCount(typeQuery, search, null,
                    null, null, null, null);

            List<MediaDto> mediaDtoList = MediaDto.fromMediaList(mediaList, uriInfo);
            Response.ResponseBuilder res = Response.ok(new GenericEntity<List<MediaDto>>(mediaDtoList) {
            });
            final PagingUtils<Media> toReturnMediaList = new PagingUtils<>(mediaList,page - 1, pageSizeQuery, mediaCount);
            ResponseUtils.setPaginationLinks(res,toReturnMediaList,uriInfo);
            return res.build();
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
            if(media.isType()){
                return Response.ok(TVSerieDto.fromTVSerie(mediaService.getTvById(id), uriInfo)).build();
            }
            return Response.ok(MovieDto.fromMovie(mediaService.getMovieById(id), uriInfo)).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }


    @GET
    @Path("/{id}/reviews")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReviewsByMediaId(@PathParam("id") final int mediaId, @QueryParam("pageNumber") @DefaultValue("1") final int page) {
        try {
            final List<Review> reviews = reviewService.getReviewsByMediaId(mediaId, PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize(), page-1);
            final List<ReviewDto> reviewDtos = ReviewDto.fromReviewList(reviews, uriInfo);
            return Response.ok(new GenericEntity<List<ReviewDto>>(reviewDtos) {}).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }


    @POST
    @Path("{id}/review")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createReview(@PathParam("id") int mediaId,@Valid final ReviewCreateDto reviewDto) {
        try {
             reviewService.createReview(
                    mediaId,
                    reviewDto.getRating(),
                    reviewDto.getReviewContent(),
                    ReviewTypes.REVIEW_MEDIA
            );

             return Response.status(Response.Status.CREATED)
                     .entity("Review successfully created to the media with ID: " + mediaId)
                     .build();

        } catch(UserNotLoggedException e){
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\":\"User must be logged in to review a movie.\"}")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An unexpected error occurred: " + e.getMessage())
                    .build();
        }
    }



    @PUT
    @Path("{id}/review")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response editReview(@PathParam("id") int mediaId,@Valid final ReviewCreateDto reviewDto) {
        try {
            reviewService.editReview(
                    mediaId,
                    reviewDto.getRating(),
                    reviewDto.getReviewContent(),
                    ReviewTypes.REVIEW_MEDIA
            );

            return Response.ok()
                    .entity("Review successfully updated for media with ID: " + mediaId)
                    .build();

        } catch (UserNotLoggedException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\":\"User must be logged in to edit a review.\"}")
                    .build();
        } catch (ReviewNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Review not found or you do not have permission to edit.\"}")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An unexpected error occurred: " + e.getMessage())
                    .build();
        }
    }
}
