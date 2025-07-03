package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.controller.UserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;
    private final UserService userService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService, UserService userService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Log request details
        LOGGER.debug("Processing request {} {}", request.getMethod(), request.getRequestURI());

        // Get authorization header and validate
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ")) {
            LOGGER.debug("No authorization header or header does not start with Bearer");
            filterChain.doFilter(request, response);
            return;
        }

        // Get JwtToken and UserDetails
        final String token = header.split(" ")[1].trim();
        final JwtTokenDetails jwtTokenDetails = jwtTokenProvider.validate(token);
        LOGGER.debug("Authorization token: {}", token);

        if (jwtTokenDetails == null) {
            LOGGER.debug("UserDetails is null after parsing token");
            filterChain.doFilter(request, response);
            return;
        } else {
            LOGGER.debug("UserDetails parsed: {}", jwtTokenDetails.getId());
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(jwtTokenDetails.getEmail());

        // Validate userDetails
        if (userDetails == null) {
            LOGGER.debug("UserDetails is null");
            filterChain.doFilter(request, response);
            return;
        } else if (!userDetails.isEnabled()) {
            LOGGER.debug("UserDetails is not enabled");
            filterChain.doFilter(request, response);
            return;
        } else if (!userDetails.isAccountNonLocked()) {
            LOGGER.debug("UserDetails account is locked");
            filterChain.doFilter(request, response);
            return;
        } else if (SecurityContextHolder.getContext().getAuthentication() != null) {
            LOGGER.debug("Security context already has authentication");
            filterChain.doFilter(request, response);
            return;
        }

        if (jwtTokenDetails.getTokenType().isRefreshToken()){
            final User potentialUser = userService.findUserByUsername(userDetails.getUsername());
            final ServletUriComponentsBuilder uriBuilder = ServletUriComponentsBuilder.fromContextPath(request);
            response.setHeader("Moovie-AuthToken", jwtTokenProvider.createAccessToken(uriBuilder, potentialUser));
            response.setHeader("Moovie-RefreshToken", jwtTokenProvider.createRefreshToken(uriBuilder, potentialUser));
        }

        // Create authentication and set it on the spring security context
        final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getAuthorities()
        );
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        LOGGER.debug("Authentication created for user: {}", userDetails.getUsername());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        LOGGER.debug("Security context updated with authentication for user: {}", userDetails.getUsername());

        filterChain.doFilter(request, response);
        LOGGER.debug("Request processing completed for {}", request.getRequestURI());
    }

}
