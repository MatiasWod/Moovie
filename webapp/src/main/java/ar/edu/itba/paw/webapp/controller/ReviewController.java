package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.PagingSizes;
import ar.edu.itba.paw.models.Review.Review;
import ar.edu.itba.paw.services.ReviewService;
import ar.edu.itba.paw.webapp.dto.out.ReviewDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
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

    @GET
    @Path("/media/{mediaId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReviewsByMediaId(@PathParam("mediaId") final int mediaId, @QueryParam("pageNumber") @DefaultValue("1") final int page) {
        try {
            final List<Review> reviews = reviewService.getReviewsByMediaId(mediaId, PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize(), page-1);
            if (reviews.isEmpty() && page == 1) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            final List<ReviewDto> reviewDtos = ReviewDto.fromReviewList(reviews, uriInfo);
            return Response.ok(new GenericEntity<List<ReviewDto>>(reviewDtos) {}).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

}
