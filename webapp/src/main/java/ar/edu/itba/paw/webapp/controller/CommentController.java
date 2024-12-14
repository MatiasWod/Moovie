package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.ReviewNotFoundException;
import ar.edu.itba.paw.exceptions.UnableToFindUserException;
import ar.edu.itba.paw.exceptions.UserNotLoggedException;
import ar.edu.itba.paw.models.Comments.Comment;
import ar.edu.itba.paw.models.Reports.CommentReport;
import ar.edu.itba.paw.models.Reports.ReviewReport;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.services.CommentService;
import ar.edu.itba.paw.services.ReportService;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.dto.in.ReportCreateDTO;
import ar.edu.itba.paw.webapp.dto.out.CommentDto;
import ar.edu.itba.paw.webapp.dto.out.ReportDTO;
import ar.edu.itba.paw.webapp.mappers.ExceptionEM;
import ar.edu.itba.paw.webapp.mappers.UnableToFindUserEM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("comment")
@Component
public class CommentController {
    private final CommentService commentService;
    private final UserService userService;
    private final ReportService reportService;

    @Context
    UriInfo uriInfo;

    @Autowired
    public CommentController(CommentService commentService, UserService userService, ReportService reportService) {
        this.commentService = commentService;
        this.userService = userService;
        this.reportService = reportService;
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


    //    -------------- Moderation ----------------
    @POST
    @Path("/{id}/report")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response reportReview(@PathParam("id") final int id,
                                 @Valid final ReportCreateDTO reportDTO) {
        try {
            User currentUser = userService.getInfoOfMyUser();
            CommentReport response = reportService.reportComment(id, currentUser.getUserId(), reportDTO.getType(), reportDTO.getContent());
            return Response.ok(ReportDTO.fromCommentReport(response, uriInfo)).build();
        } catch (UnableToFindUserException e) {
            return new UnableToFindUserEM().toResponse(e);
        } catch (Exception e) {
            return new ExceptionEM().toResponse(e);
        }

    }

    @DELETE
    @Path("/{id}/report")
    public Response resolveReport(@PathParam("id") final int commentId) {
        try {
            reportService.resolveCommentReport(commentId);
        } catch (Exception e) {
            return new ExceptionEM().toResponse(e);
        }
        return Response.ok().build();
    }


}
