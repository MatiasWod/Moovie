package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.exceptions.UnableToFindUserException;
import ar.edu.itba.paw.exceptions.UserVerifiedException;
import ar.edu.itba.paw.exceptions.authentication.UserBannedException;
import ar.edu.itba.paw.exceptions.authentication.UserNotVerifiedException;
import ar.edu.itba.paw.models.User.Token;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.models.User.UserRoles;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.services.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Component
public class BasicAuthenticationFilter extends OncePerRequestFilter{

    private static final int USR_IDX = 0;
    private static final int PWD_IDX = 1;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserService userService;
    @Autowired
    private VerificationTokenService verificationTokenService;
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Basic ")) {
            chain.doFilter(request, response);
            return;
        }

        try {
            String[] credentials = extractAndDecodeHeader(header);
            User user;
            try{
                user = userService.findUserByUsername(credentials[USR_IDX]);
            } catch (UnableToFindUserException e) {
                throw new UsernameNotFoundException("Username is incorrect");
            }
            Optional<Token> potentialToken = verificationTokenService.getToken(credentials[PWD_IDX]);

            if (potentialToken.isPresent()) {
                final Token token = potentialToken.get();
                if (token.getUserId() != user.getUserId()) {
                    throw new BadCredentialsException("Invalid token");
                }
                userService.confirmRegister(token);
                final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
                final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            else if (user.getRole() == UserRoles.NOT_AUTHENTICATED.getRole()) {
                throw new UserNotVerifiedException("User not verified");
            } else if (user.getRole() == UserRoles.BANNED.getRole()){
                throw new UserBannedException("User is banned");
            }
            else {
                final Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(credentials[USR_IDX], credentials[PWD_IDX])
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            final ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromContextPath(request);
            response.setHeader("Moovie-AuthToken", jwtTokenProvider.createAccessToken(builder,user));
            response.setHeader("Moovie-RefreshToken", jwtTokenProvider.createRefreshToken(builder,user));

        } catch (AccessDeniedException denied) {
            SecurityContextHolder.clearContext();
            accessDeniedHandler.handle(request, response, denied);
            return;
        } catch (AuthenticationException failed) {
            SecurityContextHolder.clearContext();
            authenticationEntryPoint.commence(request, response, failed);
            return;
        }

        chain.doFilter(request, response);
    }

    private String[] extractAndDecodeHeader(String header) {
        byte[] base64Token = header.split(" ")[1].trim().getBytes(StandardCharsets.UTF_8);
        byte[] decoded;
        try {
            decoded = Base64.decode(base64Token);
        } catch (IllegalArgumentException e) {
            throw new BadCredentialsException("Failed to decode basic authentication token");
        }

        String token = new String(decoded, StandardCharsets.UTF_8);

        int delim = token.indexOf(":");

        if (delim == -1) {
            throw new BadCredentialsException("Invalid basic authentication token");
        }
        return new String[]{token.substring(0, delim), token.substring(delim + 1)};

    }
}
