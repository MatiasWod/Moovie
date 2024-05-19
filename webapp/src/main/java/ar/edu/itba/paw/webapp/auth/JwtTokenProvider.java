package ar.edu.itba.paw.webapp.auth;



import ar.edu.itba.paw.models.User.User;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.core.io.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;


import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    UserDetailsServiceImpl userDetailsService;

    private static final int EXPIRY_TIME = 7 * 24 * 60 * 60 * 1000; //1 week (in millis)

    private final Key jwtKey;

    public JwtTokenProvider(Resource jwtKeyResource) throws IOException {
        this.jwtKey = Keys.hmacShaKeyFor(
                FileCopyUtils.copyToString(new InputStreamReader(jwtKeyResource.getInputStream()))
                        .getBytes(StandardCharsets.UTF_8)
        );
    }

    /**
     * jws: Json Web Signature (https://datatracker.ietf.org/doc/html/rfc7515)
     */
    public UserDetails parseToken(String jws) {
        try {
            // deprecated implementation
//            final Claims claims = Jwts.parser().setSigningKey(jwtKey).build().parseClaimsJws(jws).getBody();

            final Claims claims = Jwts.parser()
                    .verifyWith((SecretKey) jwtKey)
                    .build()
                    .parseSignedClaims(jws).getPayload();

            if (new Date(System.currentTimeMillis()).after(claims.getExpiration())) {
                return null;
            }

            final String username = claims.getSubject();

            return userDetailsService.loadUserByUsername(username);
        } catch (Exception e) {
            return null;
        }
    }

    public String createToken(User user) {

        return "Bearer " + Jwts.builder()
                .subject(user.getUsername())
                .claim("authorization", user.getRole())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRY_TIME))
                .signWith(jwtKey)
                .compact();
    }
}
