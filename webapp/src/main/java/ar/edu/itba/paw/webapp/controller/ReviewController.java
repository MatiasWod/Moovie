package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.ReviewNotFoundException;
import ar.edu.itba.paw.exceptions.UnableToFindUserException;
import ar.edu.itba.paw.exceptions.UserNotLoggedException;
import ar.edu.itba.paw.models.Review.Review;
import ar.edu.itba.paw.models.Review.ReviewTypes;
import ar.edu.itba.paw.services.ReviewService;
import ar.edu.itba.paw.webapp.dto.out.ReviewDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
//TODO CHECK LOGGERS
//import com.sun.org.slf4j.internal.Logger;
//import com.sun.org.slf4j.internal.LoggerFactory;


@Path("review")
@Component
public class ReviewController {
    private final ReviewService reviewService;

    @Context
    UriInfo uriInfo;

    //private static final Logger LOGGER = LoggerFactory.getLogger(ReviewController.class);

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }


    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReviewById(@PathParam("id") final int id) {
        try {
            final Review review = reviewService.getReviewById(id);
            if (review == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            final ReviewDto reviewDto = ReviewDto.fromReview(review, uriInfo);
            return Response.ok(reviewDto).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteReviewById(@PathParam("id") final int reviewId) {
        try {
            reviewService.deleteReview(reviewId, ReviewTypes.REVIEW_MEDIA);

            return Response.ok()
                    .entity("Review successfully deleted.")
                    .build();

        } catch (UserNotLoggedException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\":\"User must be logged in to delete a review.\"}")
                    .build();
        } catch (ReviewNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Review not found or you do not have permission to delete.\"}")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An unexpected error occurred: " + e.getMessage())
                    .build();
        }
    }

    @POST
    @Path("/{id}/like")
    @Produces(MediaType.APPLICATION_JSON)
    public Response likeReview(@PathParam("id") final int id) {
        try {
            boolean liked = reviewService.likeReview(id, ReviewTypes.REVIEW_MEDIA);
            if(liked){
                return Response.ok()
                        .entity("Review successfully liked.")
                        .build();
            }
            return Response.ok()
                    .entity("Review successfully unliked.")
                    .build();

        } catch (UserNotLoggedException | UnableToFindUserException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\":\"User must be logged in to like a review.\"}")
                    .build();
        } catch (ReviewNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Review not found.\"}")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An unexpected error occurred: " + e.getMessage())
                    .build();
        }
    }


}

//    @GET
//    @Path("/{id}/moovieListReviews")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getMoovieListReviewsFromUser(@PathParam("id") final int userId, @QueryParam("pageNumber") @DefaultValue("1") final int page) {
//        try {
//            final List<MoovieListReview> reviews = reviewService.getMoovieListReviewsFromUser(userId, PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize(), page-1);
//            final List<ReviewDto> reviewDtos = ReviewDto.fromReviewList(reviews, uriInfo);
//            return Response.ok(new GenericEntity<List<ReviewDto>>(reviewDtos) {}).build();
//        } catch (RuntimeException e) {
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
//        }
//    }

//    List<MoovieListReview> getMoovieListReviewsFromUser(int userId, int size, int pageNumber);
//    MoovieListReview getMoovieListReviewById(int moovieListReviewId);

//moovielist/1/reviews
//    List<MoovieListReview> getMoovieListReviewsByMoovieListId(int moovieListId, int size, int pageNumber) ;


