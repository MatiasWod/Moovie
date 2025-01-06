package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.exceptions.NoFileException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class NoFileEM implements ExceptionMapper<NoFileException> {
    @Override
    public Response toResponse(NoFileException e) {
        return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
    }
}