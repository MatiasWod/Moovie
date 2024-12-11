package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Provider.Provider;
import ar.edu.itba.paw.services.ProviderService;
import ar.edu.itba.paw.webapp.dto.out.ProviderDto;
import ar.edu.itba.paw.webapp.dto.out.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;
import java.util.List;

@Path("providers")
@Component
public class ProviderController {

    @Autowired
    private ProviderService providerService;

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllProviders(){
        final List<Provider> providerList = providerService.getAllProviders();
        final List<ProviderDto> providerDtoList = ProviderDto.fromProviderList(providerList,uriInfo);
        return Response.ok(new GenericEntity<List<ProviderDto>>(providerDtoList){}).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllProviders(@PathParam("id") final int mediaId){
            final List<Provider> providerList = providerService.getProvidersForMedia(mediaId);
            final List<ProviderDto> providerDtoList = ProviderDto.fromProviderList(providerList,uriInfo);
            return Response.ok(new GenericEntity<List<ProviderDto>>(providerDtoList){}).build();
    }
}
