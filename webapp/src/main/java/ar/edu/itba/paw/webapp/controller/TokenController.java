package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.services.VerificationTokenService;
import ar.edu.itba.paw.webapp.dto.in.UserCreateDto;
//TODO CHECK LOGGERS
//import com.sun.org.slf4j.internal.Logger;
//import com.sun.org.slf4j.internal.LoggerFactory;


import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


public class TokenController {
    private final UserService userService;

    private final VerificationTokenService verificationTokenService;

    //private static final Logger LOGGER = LoggerFactory.getLogger(TokenController.class);

    public TokenController(UserService userService, VerificationTokenService verificationTokenService) {
        this.userService = userService;
        this.verificationTokenService = verificationTokenService;
    }

    @POST
    @Path("/sendVerificationEmail")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sendVerificationEmail(@Valid final UserCreateDto userCreateDto) {
//        final User user = userService.findUserByEmail(userCreateDto.getEmail());
//        if(user == null)
//            return ResponseMessage.status(ResponseMessage.Status.NOT_FOUND).build();
//
//        final String token = userService.createUser(userCreateDto.getUsername(), userCreateDto.getEmail(), userCreateDto.getPassword());
//
//        userService.resendVerificationEmail(verificationTokenService.getToken(token));
        return Response.ok().build();
    }
}
