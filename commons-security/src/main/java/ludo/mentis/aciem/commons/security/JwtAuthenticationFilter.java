package ludo.mentis.aciem.commons.security;

import com.nimbusds.jwt.SignedJWT;
import ludo.mentis.aciem.commons.security.exception.InvalidSignatureException;
import ludo.mentis.aciem.commons.security.exception.PublicKeyException;
import ludo.mentis.aciem.commons.security.service.OAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;


/**
 * Custom authentication filter for handling JWT tokens.
 * <p>
 * This class extends the {@link BasicAuthenticationFilter} and provides a custom
 * authentication mechanism using an {@link OAuthService} to validate JWT tokens.
 * </p>
 * <p>
 * The authentication process involves:
 * <ul>
 *   <li>Retrieving the JWT token from the Authorization header.</li>
 *   <li>Validating the token signature and extracting the username and authorities.</li>
 *   <li>Creating a {@link JwtAuthenticationToken} with the user details and authorities.</li>
 *   <li>Setting the user as authenticated in the security context.</li>
 * </ul>
 * </p>
 * <p>
 * If authentication fails due to parsing errors, invalid signatures, or access denial,
 * an {@link IOException} is thrown with an appropriate error message.
 * </p>
 */
@Component
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    private final OAuthService oauthService;

    private final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    public JwtAuthenticationFilter(OAuthService oauthService) {
        super(authentication -> {
            authentication.setAuthenticated(true);
            return authentication;
        });
        this.oauthService = oauthService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        var header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        var token = header.substring(7); // Remove "Bearer " prefix

        SignedJWT signedJWT;
        try {
            signedJWT = oauthService.parseToken(token);
        } catch (ParseException e) {
            log.error("Unable to parse the provided token.", e);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unable to parse the provided token.");
            return;
        } catch (InvalidSignatureException | PublicKeyException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            return;
        }

        String username;
        try {
            username = oauthService.extractUsername(signedJWT);
        } catch (ParseException e) {
            log.error("Unable to parse the username from the provided token.", e);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unable to parse the username from the provided token.");
            return;
        }

        List<SimpleGrantedAuthority> authorities;
        try {
            authorities = oauthService.extractAuthorities(signedJWT);
        } catch (ParseException e) {
            log.error("Unable to parse the authorities from the provided token.", e);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unable to parse the authorities from the provided token.");
            return;
        }

        // Set the user as authenticated in the security context
        var authentication = new JwtAuthenticationToken(username, authorities);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(request, response);
    }
}
