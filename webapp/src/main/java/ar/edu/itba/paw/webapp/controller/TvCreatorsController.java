package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.PagingSizes;
import ar.edu.itba.paw.models.PagingUtils;
import ar.edu.itba.paw.models.TV.TVCreators;
import ar.edu.itba.paw.services.TVCreatorsService;
import ar.edu.itba.paw.webapp.dto.out.TvCreatorsDto;
import ar.edu.itba.paw.webapp.utils.ResponseUtils;
import ar.edu.itba.paw.webapp.vndTypes.VndType;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.NoResultException;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
@Api(value = "/tvCreators")
@Path("tvCreators")
@Component
public class TvCreatorsController {

    private final TVCreatorsService tvCreatorsService;


    @Context
    UriInfo uriInfo;

    @Autowired
    public TvCreatorsController(TVCreatorsService tvCreatorsService) {
        this.tvCreatorsService = tvCreatorsService;
    }


    @GET
    @Produces(VndType.APPLICATION_TVCREATOR_LIST)
    public Response getTVCreators(
            @QueryParam("search") final String search,
            @QueryParam("mediaId") final Integer mediaId,
            @QueryParam("pageNumber") @DefaultValue("1") final int pageNumber,
            @QueryParam("pageSize") @DefaultValue("-1") final int pageSize
    ) {
        try {
            // Determine page size
            int pageSizeQuery = pageSize;
            if (pageSize < 1 || pageSize > PagingSizes.TV_CREATOR_DEFAULT_PAGE_SIZE.getSize()) {
                pageSizeQuery = PagingSizes.TV_CREATOR_DEFAULT_PAGE_SIZE.getSize();
            }

            if (search != null && !search.isEmpty()) {
                // Lógica para obtener creadores de TV por consulta de búsqueda
                List<TVCreators> tvCreatorsList = tvCreatorsService.getTVCreatorsForQuery(search, pageNumber, pageSizeQuery);
                int totalCount = tvCreatorsService.getTVCreatorsForQueryCount(search);
                List<TvCreatorsDto> tvCreatorsDtoList = TvCreatorsDto.fromTvCreatorList(tvCreatorsList, uriInfo);
                
                Response.ResponseBuilder res = Response.ok(new GenericEntity<List<TvCreatorsDto>>(tvCreatorsDtoList) {});
                final PagingUtils<TvCreatorsDto> pagingUtils = new PagingUtils<>(tvCreatorsDtoList, pageNumber, pageSizeQuery, totalCount);
                ResponseUtils.setPaginationLinks(res, pagingUtils, uriInfo);
                return res.build();
            } else if (mediaId != null) {
                // Lógica para obtener creadores de TV por ID de medio
                List<TVCreators> tvCreators = tvCreatorsService.getTvCreatorsByMediaId(mediaId, pageNumber, pageSizeQuery);
                int totalCount = tvCreatorsService.getTvCreatorsByMediaIdCount(mediaId);
                List<TvCreatorsDto> tvCreatorsDtos = TvCreatorsDto.fromTvCreatorList(tvCreators, uriInfo);
                
                Response.ResponseBuilder res = Response.ok(new GenericEntity<List<TvCreatorsDto>>(tvCreatorsDtos) {});
                // Add pagination headers (using conservative count)
                final PagingUtils<TvCreatorsDto> pagingUtils = new PagingUtils<>(tvCreatorsDtos, pageNumber, pageSizeQuery, tvCreatorsDtos.size());
                ResponseUtils.setPaginationLinks(res, pagingUtils, uriInfo);
                ResponseUtils.setMaxAgeCache(res);
                return res.build();
            }

            // Si no se proporcionan parámetros válidos
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("You must provide either 'search' or 'mediaId' as query parameters.")
                    .build();
        }catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An error occurred while processing the request.")
                    .build();
        }

    }

    @GET
    @Path("/{id}")
    @Produces(VndType.APPLICATION_TVCREATOR)
    public Response getTvCreatorById(@PathParam("id") @NotNull final int tvCreatorId) {
        try {
            TVCreators tvCreators=tvCreatorsService.getTvCreatorById(tvCreatorId);
            Response.ResponseBuilder res = Response.ok(TvCreatorsDto.fromTvCreator(tvCreators,uriInfo));
            ResponseUtils.setMaxAgeCache(res);
            return res.build();
        }catch (NoResultException e){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("TV Creator with id " + tvCreatorId + " not found.")
                    .build();
        }
        catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An error occurred while processing the request.")
                    .build();
        }
    }
}

