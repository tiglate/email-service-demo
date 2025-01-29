package ludo.mentis.aciem.tabellarius.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.SignedJWT;
import ludo.mentis.aciem.tabellarius.service.PublicKeyService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    private final PublicKeyService publicKeyService;

    public JwtAuthenticationFilter(PublicKeyService publicKeyService) {
        super(authentication -> {
            authentication.setAuthenticated(true);
            return authentication;
        });
        this.publicKeyService = publicKeyService;
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

            var publicKey = publicKeyService.getPublicKey();
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
                    .collect(Collectors.toList());

            // Set the user as authenticated in the security context
            var authentication = new JwtAuthenticationToken(username, authorities);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (ParseException | JOSEException | PublicKeyException | RuntimeException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
            return;
        }

        chain.doFilter(request, response);
    }
}
