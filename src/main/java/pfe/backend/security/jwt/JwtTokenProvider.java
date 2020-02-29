package pfe.backend.security.jwt;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import pfe.backend.DAL.Services.CustomerUserDetailsService;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${security.jwt.token.expire-length:14400000}")
    static final long validityInMilliseconds = 14400000; // 4h
    @Value("${security.jwt.token.secret-key:jeSuisTropFort}")
    private String secretKey = "jeSuisTropFort";
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private CustomerUserDetailsService customerUserDetailsService;

    //private UserDetailsService userDetailsService;

    //public JwtTokenProvider(UserDetailsService userDetailsService) {
    //  this.userDetailsService = userDetailsService;
    //}
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    /**
     * When the user logs in or creates an account, he is created a token that he keep until the end of the expiration
     * or when he disconnects.
     *
     * @param username : username
     * @param role     : role
     * @param userId   : userId
     * @return string of the jwt
     */
    public String createToken(String username, String role, String userId, String email) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("role", role);
        claims.put("userId", userId);
        claims.put("email", email);

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()//
                .setClaims(claims)//
                .setIssuedAt(now)//
                .setExpiration(validity)//
                .signWith(SignatureAlgorithm.HS256, secretKey)//
                .compact();
    }

    /**
     * Allows the user to register with spring security
     *
     * @param token : toekn
     * @return an Authentication
     */
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = (UserDetails) this.customerUserDetailsService.loadUserByUsername(getUsername(token)); // avant c'était un userDetailsService
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /**
     * Give the information specific to the user stored in the token, deciphre the token
     *
     * @param token : token
     * @return the username
     */
    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Go to read in the header of the request http, then we just extract the token
     *
     * @param req : request emits by the front-end
     * @return the inside of the token
     */
    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

    /**
     * Check that the token is still valid, ie that it has not expired according to the date that was put in the token     * @param token
     *
     * @return true if it's Ok, false otherwise
     */
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

            if (claims.getBody().getExpiration().before(new Date())) {
                return false;
            }
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidJwtAuthenticationException("Expired or invalid JWT token");
        }
    }

}
