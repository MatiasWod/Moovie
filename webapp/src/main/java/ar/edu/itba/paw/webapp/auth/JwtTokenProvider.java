package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.models.User.UserRoles;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final int AUTHENTICATION_EXPIRATION_TIME = 10 * 60 * 1000; // 10 minutes
    private static final int REFRESH_EXPIRATION_TIME = 7 * 24 * 60 * 60 * 1000; // 1 week
    private final SecretKey jwtKey;

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenProvider.class);

    public JwtTokenProvider(Resource jwtKeyResource) throws IOException {
        this.jwtKey = Keys.hmacShaKeyFor(
                FileCopyUtils.copyToString(new InputStreamReader(jwtKeyResource.getInputStream()))
                        .getBytes(StandardCharsets.UTF_8)
        );
        LOGGER.debug("JwtTokenProvider initialized with key from resource: {}", jwtKeyResource.getFilename());
    }

    public JwtTokenDetails validate(String token) {
        try {
            final Jws<Claims> parsed = Jwts.parser().verifyWith(jwtKey).build().parseSignedClaims(token);
            final Claims claims = parsed.getPayload();

            if (claims.getExpiration().before(new Date(System.currentTimeMillis()))) {
                throw new ExpiredJwtException(parsed.getHeader(), claims, "JWT token expired");
            }

            return new JwtTokenDetails.Builder()
                    .setId(claims.getId())
                    .setToken(token)
                    .setEmail(claims.getSubject())
                    .setIssuedDate(claims.getIssuedAt())
                    .setExpirationDate(claims.getExpiration())
                    .setTokenType(JwtTokenType.fromCode((claims.get("tokenType").toString())))
                    .build();

        } catch (SignatureException ex) {
            LOGGER.warn("Invalid JWT signature - {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            LOGGER.warn("Invalid JWT token - {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            LOGGER.warn("Expired JWT token - {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            LOGGER.warn("Unsupported JWT token - {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            LOGGER.warn("JWT claims string is empty - {}", ex.getMessage());
        } catch (Exception ex) {
            LOGGER.warn("JWT claims {}", ex.getMessage());
        }
        return null;
    }

    public String createToken(ServletUriComponentsBuilder uriBuilder, User user, JwtTokenType type, int expirationTime) {
        LOGGER.debug("Creating token for user: {}", user.getUsername());

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        JwtBuilder builder = Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(now)
                .expiration(expiryDate)
                .claim("tokenType", type);

        if (!type.isRefreshToken()) {
            builder.claim("name", user.getUsername());

            if (user.getRole() != UserRoles.NOT_AUTHENTICATED.getRole()) {
                builder.claim("role", user.getRole());
            }

            final String selfUrl = uriBuilder
                    .path("/users/")
                    .path(user.getUsername())
                    .build()
                    .toString();

            builder.claim("selfUrl", selfUrl);
        }

        return builder
                .signWith(jwtKey)
                .compact();
    }



    public String createAccessToken(ServletUriComponentsBuilder uriBuilder, User user) {
        return createToken(uriBuilder, user, JwtTokenType.ACCESS, AUTHENTICATION_EXPIRATION_TIME);
    }

    public String createRefreshToken(ServletUriComponentsBuilder uriBuilder, User user) {
        return createToken(uriBuilder, user, JwtTokenType.REFRESH, REFRESH_EXPIRATION_TIME);
    }


}
