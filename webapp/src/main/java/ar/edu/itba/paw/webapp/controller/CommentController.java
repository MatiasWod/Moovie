package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.ReviewNotFoundException;
import ar.edu.itba.paw.exceptions.UnableToFindUserException;
import ar.edu.itba.paw.exceptions.UserNotLoggedException;
import ar.edu.itba.paw.models.Comments.Comment;
import ar.edu.itba.paw.models.PagingSizes;
import ar.edu.itba.paw.models.PagingUtils;
import ar.edu.itba.paw.models.Reports.CommentReport;
import ar.edu.itba.paw.models.Reports.ReviewReport;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.services.CommentService;
import ar.edu.itba.paw.services.ReportService;
import ar.edu.itba.paw.services.ReviewService;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.dto.in.CommentCreateDto;
import ar.edu.itba.paw.webapp.dto.in.ReportCreateDTO;
import ar.edu.itba.paw.webapp.dto.out.CommentDto;
import ar.edu.itba.paw.webapp.dto.out.ReportDTO;
import ar.edu.itba.paw.webapp.mappers.ExceptionEM;
import ar.edu.itba.paw.webapp.mappers.UnableToFindUserEM;
import ar.edu.itba.paw.webapp.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
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
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCommentById(@PathParam("id") int id) {
        try {
            final Comment comment = commentService.getCommentById(id);
            final CommentDto commentDto = CommentDto.fromComment(comment, uriInfo);
            return Response.ok(commentDto).build();
        } catch (RuntimeException e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCommentsByReviewId(@QueryParam("reviewId") final int reviewId, @QueryParam("pageNumber") @DefaultValue("1") final int page) {
        final int commentCount = reviewService.getReviewById(reviewId).getCommentCount().intValue();
        final List<Comment> commentList = commentService.getComments(reviewId, PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize(), page - 1);
        final List<CommentDto> commentDtoList = CommentDto.fromCommentList(commentList, uriInfo);

        Response.ResponseBuilder res = Response.ok(new GenericEntity<List<CommentDto>>(commentDtoList) {
        });
        final PagingUtils<Comment> reviewPagingUtils = new PagingUtils<>(commentList, page, PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize(), commentCount);
        ResponseUtils.setPaginationLinks(res, reviewPagingUtils, uriInfo);
        return res.build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createComment(@QueryParam("reviewId") final int reviewId, @Valid final CommentCreateDto commentDto) {
        commentService.createComment(
                reviewId,
                commentDto.getCommentContent()
        );
        return Response.status(Response.Status.CREATED)
                .entity("Comment successfully created to review with id:" + reviewId)
                .build();
    }


    @POST
    @Path("/{id}/like")
    @Produces(MediaType.APPLICATION_JSON)
    public Response likeComment(@PathParam("id") int id) {
        try {
            boolean liked = commentService.likeComment(id);
            if (liked) {
                return Response.ok()
                        .entity("Comment liked").build();
            }
            return Response.ok().entity("Comment not liked").build();
        } catch (UserNotLoggedException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\":\"User must be logged in to like a comment.\"}")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An unexpected error occurred: " + e.getMessage())
                    .build();
        }
    }



    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
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

    @DELETE
    @Path("/{id}/removeLikeComment")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeLikeComment(@PathParam("id") int id) {
        try {
            commentService.removeLikeComment(id);
            return Response.ok().entity("Comment like removed").build();
        } catch (UserNotLoggedException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\":\"User must be logged to unlike a comment.\"}")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An unexpected error occurred: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/{id}/removeUnlikeComment")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeUnlikeComment(@PathParam("id") int id) {
        try {
            commentService.removeDislikeComment(id);
            return Response.ok().entity("Comment dislike removed").build();
        } catch (UserNotLoggedException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\":\"User must be logged in to undislike a comment.\"}")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An unexpected error occurred: " + e.getMessage())
                    .build();
        }
    }


}
