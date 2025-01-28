package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.*;
import ar.edu.itba.paw.models.Comments.Comment;
import ar.edu.itba.paw.models.PagingSizes;
import ar.edu.itba.paw.models.PagingUtils;
import ar.edu.itba.paw.models.Reports.ReviewReport;
import ar.edu.itba.paw.models.Review.Review;
import ar.edu.itba.paw.models.Review.ReviewTypes;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.webapp.dto.in.CommentCreateDto;
import ar.edu.itba.paw.webapp.dto.in.ReportCreateDTO;
import ar.edu.itba.paw.webapp.dto.in.ReviewCreateDto;
import ar.edu.itba.paw.webapp.dto.out.CommentDto;
import ar.edu.itba.paw.webapp.dto.out.ReportDTO;
import ar.edu.itba.paw.webapp.dto.out.ReviewDto;
import ar.edu.itba.paw.webapp.mappers.ExceptionEM;
import ar.edu.itba.paw.webapp.mappers.InvalidAccessToResourceEM;
import ar.edu.itba.paw.webapp.mappers.UnableToFindUserEM;
import ar.edu.itba.paw.webapp.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;


//TODO CHECK LOGGERS
//import com.sun.org.slf4j.internal.Logger;
//import com.sun.org.slf4j.internal.LoggerFactory;


@Path("reviews")
@Component
public class ReviewController {
    private final ReviewService reviewService;
    private final UserService userService;

    @Context
    UriInfo uriInfo;

    //private static final Logger LOGGER = LoggerFactory.getLogger(ReviewController.class);

    @Autowired
    public ReviewController(ReviewService reviewService, UserService userService) {
        this.reviewService = reviewService;
        this.userService = userService;
    }


    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReviewById(@PathParam("id") final int id) {
        final Review review = reviewService.getReviewById(id);
        final ReviewDto reviewDto = ReviewDto.fromReview(review, uriInfo);
        return Response.ok(reviewDto).build();
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReviewsByQueryParams(
            @QueryParam("mediaId") final Integer mediaId,
            @QueryParam("userId") final Integer userId,
            @QueryParam("username") final String username,
            @QueryParam("pageNumber") @DefaultValue("1") final int page
    ) {
        if (mediaId != null && userId != null) {
            // Caso: buscar una reseña específica por mediaId y userId
            final Review review = reviewService.getReviewByMediaIdAndUsername(mediaId, userId);
            final ReviewDto reviewDto = ReviewDto.fromReview(review, uriInfo);
            return Response.ok(reviewDto).build();
        } else if (mediaId != null) {
            // Caso: buscar todas las reseñas para un mediaId con paginación
            final List<Review> reviews = reviewService.getReviewsByMediaId(mediaId, PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize(), page - 1);
            final int reviewCount = reviewService.getReviewsByMediaIdCount(mediaId);
            final List<ReviewDto> reviewDtos = ReviewDto.fromReviewList(reviews, uriInfo);
            Response.ResponseBuilder res = Response.ok(new GenericEntity<List<ReviewDto>>(reviewDtos) {});
            final PagingUtils<Review> reviewPagingUtils = new PagingUtils<>(reviews, page, PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize(), reviewCount);
            ResponseUtils.setPaginationLinks(res, reviewPagingUtils, uriInfo);
            return res.build();
        } else if (username != null) {
            try {
                final List<Review> reviews = reviewService.getMovieReviewsFromUser(userService.getProfileByUsername(username).getUserId(),
                        PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize(), page - 1);
                final List<ReviewDto> reviewDtos = ReviewDto.fromReviewList(reviews, uriInfo);
                final int reviewCount = userService.getProfileByUsername(username).getReviewsCount();

                Response.ResponseBuilder res = Response.ok(new GenericEntity<List<ReviewDto>>(reviewDtos) {
                });
                final PagingUtils<Review> reviewPagingUtils = new PagingUtils<>(reviews, page, PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize(), reviewCount);
                ResponseUtils.setPaginationLinks(res, reviewPagingUtils, uriInfo);
                return res.build();
            } catch (RuntimeException e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
            }
        }
        else {
            // Caso: parámetros inválidos o faltantes
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("At least one valid parameter is required.")
                    .build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response editReview(@QueryParam("mediaId") int mediaId,@Valid final ReviewCreateDto reviewDto) {
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

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createReview(@QueryParam("mediaId") int mediaId,@Valid final ReviewCreateDto reviewDto) {
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


    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteReviewById(@PathParam("id") final int reviewId) {
        reviewService.deleteReview(reviewId, ReviewTypes.REVIEW_MEDIA);

        return Response.ok()
                .entity("Review successfully deleted.")
                .build();

    }



    @POST
    @Path("/{id}/like")
    @Produces(MediaType.APPLICATION_JSON)
    public Response likeReview(@PathParam("id") final int id) {
        boolean liked = reviewService.likeReview(id, ReviewTypes.REVIEW_MEDIA);
        if (liked) {
            return Response.ok()
                    .entity("Review successfully liked.")
                    .build();
        }
        return Response.ok()
                .entity("Review successfully unliked.")
                .build();
    }


}

