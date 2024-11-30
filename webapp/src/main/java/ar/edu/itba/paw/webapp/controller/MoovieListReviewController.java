package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.MoovieListNotFoundException;
import ar.edu.itba.paw.exceptions.ReviewNotFoundException;
import ar.edu.itba.paw.exceptions.UnableToFindUserException;
import ar.edu.itba.paw.exceptions.UserNotLoggedException;
import ar.edu.itba.paw.models.Review.MoovieListReview;
import ar.edu.itba.paw.models.Review.ReviewTypes;
import ar.edu.itba.paw.services.ReviewService;
import ar.edu.itba.paw.webapp.dto.out.MoovieListReviewDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("moovieListReview")
@Component
public class MoovieListReviewController {
    private final ReviewService reviewService;

    @Context
    UriInfo uriInfo;

    @Autowired
    public MoovieListReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMoovieListReviewById(@PathParam("id") int id) {
        try {
            final MoovieListReview moovieListReview = reviewService.getMoovieListReviewById(id);
            final MoovieListReviewDto moovieListReviewDto = MoovieListReviewDto.fromMoovieListReview(moovieListReview, uriInfo);
            return Response.ok(moovieListReviewDto).build();
        } catch (ReviewNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"MoovieList review not found.\"}")
                    .build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteMoovieListReviewById(@PathParam("id") final int moovieListReview) {
        try {
            reviewService.deleteReview(moovieListReview, ReviewTypes.REVIEW_MOOVIE_LIST);

            return Response.ok()
                    .entity("Review successfully deleted.")
                    .build();

        } catch (UserNotLoggedException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\":\"User must be logged in to delete a review.\"}")
                    .build();
        } catch (ReviewNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"MoovieList review not found or you do not have permission to delete.\"}")
                    .build();
        } catch (MoovieListNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"MoovieList not found or you do not have permission to delete.\"}")
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
    public Response likeMoovieListReview(@PathParam("id") final int id) {
        try {
            boolean liked = reviewService.likeReview(id, ReviewTypes.REVIEW_MOOVIE_LIST);
            if (liked) {
                return Response.ok()
                        .entity("MoovieList review successfully liked.")
                        .build();
            }
            return Response.ok()
                    .entity("MoovieList review successfully unliked.")
                    .build();

        } catch (UserNotLoggedException | UnableToFindUserException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\":\"User must be logged in to like a review.\"}")
                    .build();
        } catch (ReviewNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"MoovieList review not found.\"}")
                    .build();
        } catch (MoovieListNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"MoovieList not found or you do not have permission to delete.\"}")
                    .build();
        }catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An unexpected error occurred: " + e.getMessage())
                    .build();
        }
    }

}






