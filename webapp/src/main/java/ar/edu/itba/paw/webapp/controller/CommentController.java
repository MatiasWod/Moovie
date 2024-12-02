package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.ReviewNotFoundException;
import ar.edu.itba.paw.exceptions.UserNotLoggedException;
import ar.edu.itba.paw.models.Comments.Comment;
import ar.edu.itba.paw.services.CommentService;
import ar.edu.itba.paw.webapp.dto.out.CommentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("comment")
@Component
public class CommentController {
    private final CommentService commentService;

    @Context
    UriInfo uriInfo;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCommentById(@PathParam("id") int id) {
        try {
            final Comment comment = commentService.getCommentById(id);
            final CommentDto commentDto = CommentDto.fromComment(comment,uriInfo);
            return Response.ok(commentDto).build();
        }catch (RuntimeException e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/{id}/like")
    @Produces(MediaType.APPLICATION_JSON)
    public Response likeComment(@PathParam("id") int id) {
        try {
            boolean liked = commentService.likeComment(id);
            if (liked){
                return Response.ok()
                        .entity("Comment liked").build();
            }
            return Response.ok().entity("Comment not liked").build();
        }catch(UserNotLoggedException e){
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
        }catch(UserNotLoggedException e){
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
        }catch(UserNotLoggedException e){
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
        }catch(UserNotLoggedException e){
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
