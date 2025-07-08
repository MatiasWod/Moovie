package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Genre.Genre;
import ar.edu.itba.paw.models.PagingSizes;
import ar.edu.itba.paw.models.PagingUtils;
import ar.edu.itba.paw.models.Provider.Provider;
import ar.edu.itba.paw.services.ProviderService;
import ar.edu.itba.paw.webapp.dto.out.ActorDto;
import ar.edu.itba.paw.webapp.dto.out.GenreDto;
import ar.edu.itba.paw.webapp.dto.out.ProviderDto;
import ar.edu.itba.paw.webapp.dto.out.ResponseMessage;
import ar.edu.itba.paw.webapp.utils.ResponseUtils;
import ar.edu.itba.paw.webapp.vndTypes.VndType;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Api(value = "/providers")
@Path("providers")
@Component
public class ProviderController {

    @Autowired
    private ProviderService providerService;

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(VndType.APPLICATION_PROVIDER_LIST)
    public Response getProviders(@QueryParam("mediaId") final Integer mediaId,@QueryParam("pageNumber") @DefaultValue("1") final Integer pageNumber, @QueryParam("pageSize") @DefaultValue("-1") final Integer pageSize) {

        List<Provider> providerList;

        int pageSizeQuery = pageSize;
        if (pageSize < 1 || pageSize > PagingSizes.PROVIDER_DEFAULT_PAGE_SIZE.getSize()) {
            pageSizeQuery = PagingSizes.PROVIDER_DEFAULT_PAGE_SIZE.getSize();
        }

        int totalCount=0;
        // Si se proporciona un mediaId, obtener proveedores para ese medio
        if (mediaId != null) {
            providerList = providerService.getProvidersForMedia(mediaId,pageNumber,pageSizeQuery);
            totalCount = providerService.getProvidersForMediaCount(mediaId);
        }
        // Si no se proporciona un mediaId, obtener todos los proveedores
        else {
            providerList = providerService.getAllProviders(pageNumber, pageSizeQuery);
            totalCount = providerService.getAllProvidersCount();
        }

        // Convertir la lista de proveedores a DTOs
        final List<ProviderDto> providerDtoList = ProviderDto.fromProviderList(providerList, uriInfo);

        // Devolver la respuesta con la lista de DTOs
        Response.ResponseBuilder res = Response.ok(new GenericEntity<List<ProviderDto>>(providerDtoList) {
        });
        final PagingUtils<ProviderDto> providerDtoPagingUtils = new PagingUtils<>(providerDtoList, pageNumber, pageSizeQuery,
                totalCount);
        ResponseUtils.setPaginationLinks(res, providerDtoPagingUtils, uriInfo);
        ResponseUtils.setMaxAgeCache(res);
        return res.build();

    }

    @GET()
    @Path("/{providerId}")
    @Produces(VndType.APPLICATION_PROVIDER)
    public Response getGenre(@PathParam("providerId") final Integer providerId) {
        Provider genre = providerService.getProviderById(providerId);
        Response.ResponseBuilder res = Response.ok(ProviderDto.fromProvider(genre, uriInfo));
        ResponseUtils.setMaxAgeCache(res);
        return res.build();
    }
}
