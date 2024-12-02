package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.ReviewAlreadyCreatedException;
import ar.edu.itba.paw.exceptions.ReviewNotFoundException;
import ar.edu.itba.paw.exceptions.UnableToFindUserException;
import ar.edu.itba.paw.exceptions.UserNotLoggedException;
import ar.edu.itba.paw.models.Comments.Comment;
import ar.edu.itba.paw.models.PagingSizes;
import ar.edu.itba.paw.models.PagingUtils;
import ar.edu.itba.paw.models.Review.Review;
import ar.edu.itba.paw.models.Review.ReviewTypes;
import ar.edu.itba.paw.services.CommentService;
import ar.edu.itba.paw.services.ReviewService;
import ar.edu.itba.paw.webapp.dto.in.CommentCreateDto;
import ar.edu.itba.paw.webapp.dto.out.CommentDto;
import ar.edu.itba.paw.webapp.dto.out.ReviewDto;
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


@Path("review")
@Component
public class ReviewController {
    private final ReviewService reviewService;
    private final CommentService commentService;

    @Context
    UriInfo uriInfo;

    //private static final Logger LOGGER = LoggerFactory.getLogger(ReviewController.class);

    @Autowired
    public ReviewController(ReviewService reviewService, CommentService commentService) {
        this.reviewService = reviewService;
        this.commentService=commentService;
    }


    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReviewById(@PathParam("id") final int id) {
        try {
            final Review review = reviewService.getReviewById(id);
            final ReviewDto reviewDto = ReviewDto.fromReview(review, uriInfo);
            return Response.ok(reviewDto).build();
        }catch (ReviewNotFoundException e){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Review not found.\"}")
                    .build();
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

    /* COMMENTS */
    @GET
    @Path("/{id}/comments")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getComments(@PathParam("id") final int id, @QueryParam("pageNumber") @DefaultValue("1") final int page) {
        try{
            final int commentCount=reviewService.getReviewById(id).getCommentCount().intValue();
            final List<Comment> commentList= commentService.getComments(id, PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize(),page);
            final List<CommentDto> commentDtoList=CommentDto.fromCommentList(commentList,uriInfo);

            Response.ResponseBuilder res = Response.ok(new GenericEntity<List<CommentDto>>(commentDtoList) {});
            final PagingUtils<Comment> reviewPagingUtils = new PagingUtils<>(commentList,page,PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize(),commentCount);
            ResponseUtils.setPaginationLinks(res,reviewPagingUtils,uriInfo);
            return res.build();
        } catch(ReviewNotFoundException e){
            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"error\":\"Review doesn't exists.\"}")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An unexpected error occurred: " + e.getMessage())
                    .build();
        }
    }

    @POST
    @Path("/{id}/comment")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createComment(@PathParam("id") final int id,@Valid final CommentCreateDto commentDto){
        try {
            commentService.createComment(
                    id,
                    commentDto.getCommentContent()
            );
            return Response.status(Response.Status.CREATED)
                    .entity("Comment successfully created to review with id:" + id)
                    .build();
        }catch(UserNotLoggedException e){
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\":\"User must be logged in to create a comment.\"}")
                    .build();
        } catch(ReviewNotFoundException e){
            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"error\":\"Review doesn't exists.\"}")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An unexpected error occurred: " + e.getMessage())
                    .build();
        }
    }

}

