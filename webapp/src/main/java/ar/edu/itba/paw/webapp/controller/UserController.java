package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.stream.Collectors;

@Path("users")
@Component
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(final UserService userService){
        this.userService = userService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listAll(@QueryParam("page") @DefaultValue("1") final int page) {
//        if (page<DEFAULT_PAGE_INT)
//            return Response.status(Response.Status.BAD_REQUEST).build();

        final List<User> all = userService.listAll(page);

        List<UserDto> dtoList = all.stream().map(UserDto::fromUser).collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<UserDto>>(dtoList) {}).build();
    }

    public void create(){}

}