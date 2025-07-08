package ar.edu.itba.paw.webapp.mappers;

import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.persistence.NoResultException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import ar.edu.itba.paw.webapp.dto.out.ResponseMessage;

@Singleton
@Component
@Provider
public class NoResultExceptionEM implements ExceptionMapper<NoResultException> {

    @Override
    public Response toResponse(NoResultException e) {

        return Response.status(Response.Status.NOT_FOUND).entity(new ResponseMessage(e.getMessage())).build();
    }
}
