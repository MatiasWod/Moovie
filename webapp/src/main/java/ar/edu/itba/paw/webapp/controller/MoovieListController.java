package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.*;
import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.models.MoovieList.MoovieListTypes;
import ar.edu.itba.paw.models.PagingSizes;
import ar.edu.itba.paw.services.MoovieListService;
import ar.edu.itba.paw.webapp.dto.in.MediaListDto;
import ar.edu.itba.paw.webapp.dto.in.MoovieListCreateDto;
import ar.edu.itba.paw.webapp.dto.out.MediaDto;
import ar.edu.itba.paw.webapp.dto.out.MoovieListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("list")
@Component
public class MoovieListController {

    private final MoovieListService moovieListService;

    @Context
    UriInfo uriInfo;

    @Autowired
    public MoovieListController(MoovieListService moovieListService) {
        this.moovieListService = moovieListService;
    }


    /**
     * GET METHODS
     */

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMoovieList(@QueryParam("search") String search,
                                  @QueryParam("ownerUsername") String ownerUsername,
                                  @QueryParam("type") @DefaultValue("-1") int type,
                                  @QueryParam("orderBy") String orderBy,
                                  @QueryParam("order") String order,
                                  @QueryParam("pageNumber") @DefaultValue("1") final int pageNumber) {
        try {
            if (type < 1 || type > 4) {
                type = MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.getType();
            }
            List<MoovieListDto> mlcDto = MoovieListDto.fromMoovieListList(moovieListService.getMoovieListCards(search, ownerUsername, type, orderBy, order, PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CARDS.getSize(), pageNumber), uriInfo);
            return Response.ok(new GenericEntity<List<MoovieListDto>>(mlcDto) {
            }).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMoovieListById(@PathParam("id") final int id) {
        try {
            System.out.println("ashuaa");
            return Response.ok(MoovieListDto.fromMoovieList(moovieListService.getMoovieListCardById(id), uriInfo)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    //We have a separate endpoint for content to be able to use filters and no need to do it every time we want to find a list
    @GET
    @Path("/{id}/content")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMoovieListMedia(@PathParam("id") final int id,
                                       @QueryParam("orderBy") @DefaultValue("customOrder") final String orderBy,
                                       @QueryParam("sortOrder") @DefaultValue("DESC") final String sortOrder,
                                       @QueryParam("pageNumber") @DefaultValue("1") final int pageNumber,
                                       @QueryParam("pageSize") @DefaultValue("-1") final int pageSize) {
        try {
            int pageSizeQuery = pageSize;
            if (pageSize < 1 || pageSize > PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CONTENT.getSize()) {
                pageSizeQuery = PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CONTENT.getSize();
            }

            List<Media> mediaList = moovieListService.getMoovieListContent(id, orderBy, sortOrder, pageSizeQuery, pageNumber);
            List<MediaDto> mediaDtoList = MediaDto.fromMediaList(mediaList, uriInfo);
            return Response.ok(new GenericEntity<List<MediaDto>>(mediaDtoList) {
            }).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


    /**
     * POST METHODS
     */

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createMoovieList(@Valid final MoovieListCreateDto listDto) {
        try {
            int listId = moovieListService.createMoovieList(
                    listDto.getName(),
                    MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.getType(),
                    listDto.getDescription()
            ).getMoovieListId();
            UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().path(String.valueOf(listId));

            return Response.created(uriBuilder.build())
                    .entity("{\"message\":\"Movie list created successfully.\", \"url\": \"" + uriBuilder.build().toString() + "\"}")
                    .build();


        } catch (DuplicateKeyException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("A movie list with the same name already exists.")
                    .build();

        } catch (UserNotLoggedException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("User must be logged in to create a movie list.")
                    .build();

        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An unexpected error occurred: " + e.getMessage())
                    .build();
        }
    }


    @POST
    @Path("/{moovieListId}/content")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertMediaIntoMoovieList(@PathParam("moovieListId") int moovieListId, @Valid MediaListDto mediaIdListDto) {
        List<Integer> mediaIdList = mediaIdListDto.getMediaIdList();
        try {
            MoovieList updatedList = moovieListService.insertMediaIntoMoovieList(moovieListId, mediaIdList);

            if (mediaIdList == null || mediaIdList.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("No media IDs provided.")
                        .build();
            }

            return Response.ok(updatedList).entity("Media added succesfully to the list.").build();
        } catch (MoovieListNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Movie list not found for ID: " + moovieListId)
                    .build();
        } catch (MediaNotFoundException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid media IDs provided.")
                    .build();
        } catch (UnableToInsertIntoDatabase e) {
            if (mediaIdList.size() == 1) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Media already in list.")
                        .build();
            }
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("One of the media provided already in the list.")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An unexpected error occurred: " + e.getMessage())
                    .build();
        }
    }


    /**
     * DELETE METHODS
     */

    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteMoovieList(@PathParam("id") final int id) {
        try {
            moovieListService.deleteMoovieList(id);
            return Response.noContent().build();
        } catch (UserNotLoggedException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\":\"User must be logged in to delete a movie list.\"}")
                    .build();
        } catch (InvalidAccessToResourceException e) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("{\"error\":\"User does not have permission to delete this movie list.\"}")
                    .build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"An unexpected error occurred. Please try again later.\"}")
                    .build();
        }
    }


    @DELETE
    @Path("/{id}/content")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteMediaMoovieList(@PathParam("id") final int id, @Valid MediaListDto mediaIdListDto) {
        List<Integer> mediaIdList = mediaIdListDto.getMediaIdList();
        try {
            for (int media: mediaIdList){
                moovieListService.deleteMediaFromMoovieList(id, media);
            }
            return Response.noContent().build();
        } catch ( MoovieListNotFoundException e ){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Movie list not found for ID: " + id)
                    .build();
        } catch (UserNotLoggedException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\":\"User must be logged in to delete media from a movie list.\"}")
                    .build();
        } catch (InvalidAccessToResourceException e) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("{\"error\":\"User does not have permission to delete this media from the movie list.\"}")
                    .build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"An unexpected error occurred. Please try again later.\"}")
                    .build();
        }
    }




/*
    @POST
    @Path("/{id}/content")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addMediaToMoovieList(@PathParam("moovieListId") final int listId, @PathParam("mediaId") final int mediaId) {
        try {

            moovieListService.insertMediaIntoMoovieList(listId, mediaId);

            return Response.status(Response.Status.CREATED)
                    .entity("Media successfully added to the list with ID: " + listId)
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid media or list ID: " + e.getMessage())
                    .build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An error occurred while adding media to the list: " + e.getMessage())
                    .build();
        }
    }
*/
}
