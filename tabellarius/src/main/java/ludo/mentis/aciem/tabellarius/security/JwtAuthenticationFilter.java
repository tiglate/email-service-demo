package ludo.mentis.aciem.tabellarius.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    private final PublicKey publicKey;

    public JwtAuthenticationFilter(PublicKey publicKey) {
        super(authentication -> {
            authentication.setAuthenticated(true);
            return authentication;
        });
        this.publicKey = publicKey;
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

        try {
            var signedJWT = SignedJWT.parse(token);
            var jwsObject = JWSObject.parse(token);

            var verifier = new RSASSAVerifier((RSAPublicKey) publicKey);
            if (!signedJWT.verify(verifier)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token signature");
                return;
            }

            var payload = jwsObject.getPayload().toJSONObject();
            var username = payload != null ? payload.get("sub").toString() : null;
            var roles = payload != null ? (List<?>) payload.get("roles") : Collections.emptyList();

            if (username == null || username.trim().isEmpty()) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid user");
                return;
            }

            // Convert roles to GrantedAuthority objects
            var authorities = roles.stream()
                    .map(role -> (GrantedAuthority) role::toString)
                    .toList();

            // Set the user as authenticated in the security context
            var authentication = new JwtAuthenticationToken(username, authorities);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (ParseException | JOSEException | RuntimeException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
            return;
        }

        chain.doFilter(request, response);
    }
}
