package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.*;
import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.models.MoovieList.MoovieListCard;
import ar.edu.itba.paw.models.MoovieList.MoovieListTypes;
import ar.edu.itba.paw.models.PagingSizes;
import ar.edu.itba.paw.models.PagingUtils;
import ar.edu.itba.paw.models.Reports.MoovieListReport;
import ar.edu.itba.paw.models.Review.MoovieListReview;
import ar.edu.itba.paw.models.Review.ReviewTypes;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.services.MoovieListService;
import ar.edu.itba.paw.services.ReportService;
import ar.edu.itba.paw.services.ReviewService;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.dto.in.*;
import ar.edu.itba.paw.webapp.dto.out.*;
import ar.edu.itba.paw.webapp.mappers.ExceptionEM;
import ar.edu.itba.paw.webapp.mappers.UnableToFindUserEM;
import ar.edu.itba.paw.webapp.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.ArrayList;
import java.util.List;

@Path("list")
@Component
public class MoovieListController {

    private final MoovieListService moovieListService;

    @Context
    UriInfo uriInfo;

    @Autowired
    public MoovieListController(MoovieListService moovieListService){
        this.moovieListService = moovieListService;
    }


    /**
     * GET METHODS
     */

    @GET
    @Path("/search")
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
            List<MoovieListCard> moovieListCardList = moovieListService.getMoovieListCards(search, ownerUsername, type, orderBy, order, PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CARDS.getSize(), pageNumber);
            final int moovieListCardCount = moovieListService.getMoovieListCardsCount(search, ownerUsername, type);
            List<MoovieListDto> mlcDto = MoovieListDto.fromMoovieListList(moovieListCardList, uriInfo);
            Response.ResponseBuilder res = Response.ok(new GenericEntity<List<MoovieListDto>>(mlcDto) {
            });
            final PagingUtils<MoovieListCard> toReturnMoovieListCardList = new PagingUtils<>(moovieListCardList, pageNumber, PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CARDS.getSize(), moovieListCardCount);
            ResponseUtils.setPaginationLinks(res, toReturnMoovieListCardList, uriInfo);
            return res.build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMoovieListByIds(@QueryParam("ids") final String ids) {
        if (ids.length() > 100) {
            throw new IllegalArgumentException("Invalid ids, param. A comma separated list of Media IDs. Up to 100 are allowed in a single request.");
        }
        List<Integer> idList = new ArrayList<>();
        if (ids != null && !ids.isEmpty()) {
            String[] splitIds = ids.split(",");
            for (String id : splitIds) {
                try {
                    idList.add(Integer.parseInt(id.trim()));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid ids, param. A comma separated list of Media IDs. Up to 100 are allowed in a single request.");
                }
            }
        }
        if(idList.size() >= 25 || idList.size() < 0 ) {
            throw new IllegalArgumentException("Invalid ids, param. A comma separated list of Media IDs. Up to 100 are allowed in a single request.");
        }
        List<MoovieListDto> mlList = new ArrayList<>();
        for (int id : idList) {
            MoovieListDto mlc = MoovieListDto.fromMoovieList(moovieListService.getMoovieListCardById(id),uriInfo);
            mlList.add(mlc);
        }
        return Response.ok(new GenericEntity<List<MoovieListDto>>(mlList) {}).build();
    }


    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMoovieListById(@PathParam("id") final int id) {
        return Response.ok(MoovieListDto.fromMoovieList(moovieListService.getMoovieListCardById(id), uriInfo)).build();
    }


    //We have a separate endpoint for content to be able to use filters and no need to do it every time we want to find a list
    // PROBLEM WHEN SORT ORDER AND OR ORDER BY ARE NULL
    @GET
    @Path("/{id}/content")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMoovieListMedia(@PathParam("id") final int id,
                                                        @QueryParam("orderBy") @DefaultValue("customOrder") final String orderBy,
                                                        @QueryParam("sortOrder") @DefaultValue("DESC") final String sortOrder,
                                                        @QueryParam("pageNumber") @DefaultValue("1") final int pageNumber,
                                                        @QueryParam("pageSize") @DefaultValue("-1") final int pageSize) {
        int pageSizeQuery = pageSize;
        if (pageSize < 1 || pageSize > PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CONTENT.getSize()) {
            pageSizeQuery = PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CONTENT.getSize();
        }

        List<Media> mediaList = moovieListService.getMoovieListContent(id, orderBy, sortOrder, pageSizeQuery, pageNumber);
        final int mediaCount = moovieListService.getMoovieListCardById(id).getSize();
        List<MediaIdListIdDto> dtoList = MediaIdListIdDto.fromMediaList(mediaList, id);
        Response.ResponseBuilder res = Response.ok(new GenericEntity<List<MediaIdListIdDto>>(dtoList) {
        });
        final PagingUtils<MediaIdListIdDto> toReturnMediaIdListId = new PagingUtils<>(dtoList, pageNumber, pageSizeQuery, mediaCount);
        ResponseUtils.setPaginationLinks(res, toReturnMediaIdListId, uriInfo);
        return res.build();
    }

    @GET
    @Path("/{id}/content/{mediaId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMoovieListMediaByMediaId(@PathParam("id") final int id,
                                                @PathParam("mediaId") final int mediaId ){
        if(moovieListService.isMediaInMoovieList(mediaId,id)){
            return Response.ok(new MediaIdListIdDto(mediaId, id)).build();
        }
        return Response.noContent().build();
    }

    //Only returns the 5 more relatedLists. They are related in
    @GET
    @Path("{id}/recommendedLists")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRecommendedLists(@PathParam("id") final int id) {
        List<MoovieListDto> mlcList = MoovieListDto.fromMoovieListList(moovieListService.getRecommendedMoovieListCards(id, 4, 0), uriInfo);
        Response.ResponseBuilder res = Response.ok(new GenericEntity<List<MoovieListDto>>(mlcList) {
        });
        return res.build();
    }

    //Only returns the 5 more relatedLists. They are related in
    @GET
    @Path("{id}/recommendedMedia")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRecommendedMediaToAdd(@PathParam("id") final int id) {
        List<MediaDto> mlcList = MediaDto.fromMediaList(moovieListService.getRecommendedMediaToAdd(id, 5), uriInfo);
        Response.ResponseBuilder res = Response.ok(new GenericEntity<List<MediaDto>>(mlcList) {
        });
        return res.build();

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
    public Response insertMediaIntoMoovieList(@PathParam("moovieListId") int moovieListId,
                                                               @Valid MediaListDto mediaIdListDto) {
        List<Integer> mediaIdList = mediaIdListDto.getMediaIdList();
        MoovieList updatedList = moovieListService.insertMediaIntoMoovieList(moovieListId, mediaIdList);
        if (mediaIdList == null || mediaIdList.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ResponseMessage("No media IDs provided."))
                    .build();
        }
        return Response.ok(updatedList).entity(new ResponseMessage("Media added successfully to the list.")).build();
    }

    /**
     * PUT METHODS
     */

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response editMoovieList(@PathParam("id") int listId,
                                                @Valid final EditListDTO editListForm) {
        moovieListService.editMoovieList(listId, editListForm.getListName(), editListForm.getListDescription());

        return Response.ok()
                .entity("MoovieList successfully edited for MoovieList with ID: " + listId)
                .build();
    }


    /**
     * DELETE METHODS
     */

    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteMoovieList(@PathParam("id") final int id) {
        moovieListService.deleteMoovieList(id);
        return Response.noContent().build();
    }


    @DELETE
    @Path("/{id}/content/{mediaId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteMediaMoovieList(@PathParam("id") final int id, @PathParam("mediaId") final int mId) {
        moovieListService.deleteMediaFromMoovieList(id, mId);
        return Response.noContent().build();
    }



/*
    @POST
    @Path("/{id}/content")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseMessage addMediaToMoovieList(@PathParam("moovieListId") final int listId, @PathParam("mediaId") final int mediaId) {
        try {

            moovieListService.insertMediaIntoMoovieList(listId, mediaId);

            return ResponseMessage.status(ResponseMessage.Status.CREATED)
                    .entity("Media successfully added to the list with ID: " + listId)
                    .build();
        } catch (IllegalArgumentException e) {
            return ResponseMessage.status(ResponseMessage.Status.BAD_REQUEST)
                    .entity("Invalid media or list ID: " + e.getMessage())
                    .build();
        } catch (RuntimeException e) {
            return ResponseMessage.status(ResponseMessage.Status.INTERNAL_SERVER_ERROR)
                    .entity("An error occurred while adding media to the list: " + e.getMessage())
                    .build();
        }
    }
*/
}
