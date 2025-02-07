package ludo.mentis.aciem.commons.security;

import com.nimbusds.jwt.SignedJWT;
import ludo.mentis.aciem.commons.security.exception.InvalidSignatureException;
import ludo.mentis.aciem.commons.security.exception.PublicKeyException;
import ludo.mentis.aciem.commons.security.service.OAuthService;
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

@Component
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    private final OAuthService oauthService;

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
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unable to parse the username from the provided token.");
            return;
        }

        List<SimpleGrantedAuthority> authorities;
        try {
            authorities = oauthService.extractAuthorities(signedJWT);
        } catch (ParseException e) {
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
