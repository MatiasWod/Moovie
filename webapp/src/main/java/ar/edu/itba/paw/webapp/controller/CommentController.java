package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.UserNotLoggedException;
import ar.edu.itba.paw.models.Comments.Comment;
import ar.edu.itba.paw.models.Comments.CommentFeedbackType;
import ar.edu.itba.paw.models.PagingSizes;
import ar.edu.itba.paw.models.PagingUtils;
import ar.edu.itba.paw.services.CommentService;
import ar.edu.itba.paw.services.ReviewService;
import ar.edu.itba.paw.webapp.dto.in.CommentCreateDto;
import ar.edu.itba.paw.webapp.dto.in.CommentFeedbackDto;
import ar.edu.itba.paw.webapp.dto.out.CommentDto;
import ar.edu.itba.paw.webapp.utils.ResponseUtils;
import ar.edu.itba.paw.webapp.vndTypes.VndType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("comments")
@Component
public class CommentController {
    private final CommentService commentService;
    private final ReviewService reviewService;

    @Context
    UriInfo uriInfo;

    @Autowired
    public CommentController(CommentService commentService, ReviewService reviewService) {
        this.commentService = commentService;
        this.reviewService = reviewService;
    }

    @GET
    @Produces(VndType.APPLICATION_COMMENT_LIST)
    public Response getCommentsByReviewId(@QueryParam("reviewId") final int reviewId, @QueryParam("pageNumber") @DefaultValue("1") final int page) {
        final int commentCount = reviewService.getReviewById(reviewId).getCommentCount().intValue();
        final List<Comment> commentList = commentService.getComments(reviewId, PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize(), page - 1);
        final List<CommentDto> commentDtoList = CommentDto.fromCommentList(commentList, uriInfo);

        Response.ResponseBuilder res = Response.ok(new GenericEntity<List<CommentDto>>(commentDtoList){} );
        final PagingUtils<Comment> reviewPagingUtils = new PagingUtils<>(commentList, page, PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize(), commentCount);
        ResponseUtils.setPaginationLinks(res, reviewPagingUtils, uriInfo);
        return res.build();
    }

    @GET
    @Path("/{id}")
    @Produces(VndType.APPLICATION_COMMENT)
    public Response getCommentById(@PathParam("id") int id) {
        try {
            final Comment comment = commentService.getCommentById(id);
            final CommentDto commentDto = CommentDto.fromComment(comment, uriInfo);
            return Response.ok(commentDto).build();
        } catch (RuntimeException e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }




    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(VndType.APPLICATION_COMMENT_FORM)
    public Response createComment(@QueryParam("reviewId") final int reviewId, @Valid final CommentCreateDto commentDto) {
        commentService.createComment(
                reviewId,
                commentDto.getCommentContent()
        );
        return Response.status(Response.Status.CREATED)
                .entity("Comment successfully created to review with id:" + reviewId)
                .build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(VndType.APPLICATION_COMMENT_FEEDBACK_FORM)
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateFeedbackOnComment(@PathParam("id") int id, @Valid @NotNull final CommentFeedbackDto commentFeedbackDto) {
        try {
            CommentFeedbackType commentFeedbackType=commentFeedbackDto.transformToEnum();
            if (commentFeedbackType == CommentFeedbackType.UNLIKE) {
                commentService.removeLikeComment(id);
                return Response.ok().entity("Like removed from comment").build();
            } else if (commentFeedbackType == CommentFeedbackType.UNDISLIKE) {
                commentService.removeDislikeComment(id);
                return Response.ok().entity("Dislike removed from comment").build();
            }else if (commentFeedbackType == CommentFeedbackType.LIKE) {
                commentService.likeComment(id);
                return Response.ok().entity("Comment liked").build();
            } else if (commentFeedbackType == CommentFeedbackType.DISLIKE) {
                commentService.dislikeComment(id);
                return Response.ok().entity("Comment disliked").build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Invalid feedback type")
                        .build();
            }
        } catch (UserNotLoggedException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\":\"User must be logged in to update a comment.\"}")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An unexpected error occurred: " + e.getMessage())
                    .build();
        }

    }


    @DELETE
    @Path("/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteComment(@PathParam("id") int id) {
            try {
                commentService.deleteComment(id);
                return Response.ok().entity("Comment deleted").build();
            } catch (UserNotLoggedException e) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("{\"error\":\"User must be logged in to delete a comment.\"}")
                        .build();
            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("An unexpected error occurred: " + e.getMessage())
                        .build();
            }
        }


}
